package com.sample.mainapplication.model

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sample.mainapplication.networking.LoginResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val auth = Firebase.auth
    private val userId: String? = auth.currentUser?.uid
    private val imageStorageRef = userId?.let { Firebase.storage.reference.child("images").child(it) }
    private val userDatabaseRef = userId?.let { Firebase.database.getReference("user").child(it) }

    val user: Flow<User> = callbackFlow {
        val firebaseDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                if (user != null) {
                    trySend(user)
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        userDatabaseRef?.addValueEventListener(firebaseDataListener)
        awaitClose {
            userDatabaseRef?.removeEventListener(firebaseDataListener)
        }
    }

    suspend fun createUser(
        email: String,
        password: String,
    ): LoginResult {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val currentUser = auth.currentUser

            if (currentUser != null) {
                val newUser = User(
                    userId = currentUser.uid,
                    email = email,
                )
                userDatabaseRef?.child(currentUser.uid)?.setValue(newUser)?.await()
            }
        } catch (e: Exception) {
            return LoginResult.ERROR(e.message ?: "unknown error")
        }
        return LoginResult.SUCCESS
    }

    suspend fun login(
        email: String,
        password: String,
    ): LoginResult {
        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            return LoginResult.ERROR(e.message ?: "unknown error")
        }
        return LoginResult.SUCCESS
    }

    suspend fun updateUserName(
        name: String,
    ) : LoginResult {
        return try {
            userId?.let {
                userDatabaseRef?.child("userName")?.setValue(name)?.await()
            }
            LoginResult.SUCCESS
        }  catch (e : Exception) {
            LoginResult.ERROR(e.message.toString())
        }
    }

    fun updateUserPhotoUriOnFirebase(userPhotoUri: Uri) {
        val userPhotoUriStorageRef = imageStorageRef?.child("profile_image")

        try {
            userDatabaseRef!!.child("photoUrl").setValue(userPhotoUri.toString())
            userPhotoUriStorageRef!!
                .putFile(userPhotoUri)
        } catch (e: Exception) {
            Log.d("sammy", "error = ${e.message}")
        }
    }

    fun signOut() {
        auth.signOut()
    }
}

