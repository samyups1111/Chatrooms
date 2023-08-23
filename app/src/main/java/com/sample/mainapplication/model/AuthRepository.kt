package com.sample.mainapplication.model

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.sample.mainapplication.networking.LoginResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val userId: String?,
    private val imageStorageRef: StorageReference?,
    private val userDatabaseRef: DatabaseReference?,
) {
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

