package com.sample.mainapplication.model

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.sample.mainapplication.networking.LoginResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val auth = Firebase.auth
    private val firebaseStorageRef = Firebase.storage.reference

    val user = flow {
        emit(auth.currentUser!!)
    }.map {
        User(
            userId = it.uid,
            userName = it.displayName ?: "unknown",
            profileImgUri = it.photoUrl,
        )
    }

    suspend fun createUser(
        email: String?,
        password: String?,
    ): LoginResult {
        if (email == null || email == "") return LoginResult.MISSING_USERNAME()
        if (password == null || password == "") return LoginResult.MISSING_PASSWORD()

        try {
            auth.createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            return LoginResult.ERROR(e.message ?: "unknown error")
        }
        return LoginResult.SUCCESS
    }

    suspend fun login(
        email: String?,
        password: String?,
    ): LoginResult {
        if (email == null || email == "") return LoginResult.MISSING_USERNAME()
        if (password == null || password == "") return LoginResult.MISSING_PASSWORD()

        try {
            auth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            return LoginResult.ERROR(e.message ?: "unknown error")
        }
        return LoginResult.SUCCESS
    }

    suspend fun updateUser(
        name: String,
    ) : LoginResult {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        return try {
            user!!.updateProfile(profileUpdates).await()
            LoginResult.SUCCESS
        }  catch (e : Exception) {
            LoginResult.ERROR(e.message.toString())
        }
    }

    fun saveLocalProfileImgUriToFirebase(localProfileImgUri: Uri) {
        val currentUser = auth.currentUser
        val firebaseImgStorageRef: StorageReference = firebaseStorageRef.child("images")
        if (currentUser != null && localProfileImgUri.lastPathSegment != null) {
            val firebaseProfileImgStorageRef = firebaseImgStorageRef.child(currentUser.uid).child("profile_image").child(localProfileImgUri.lastPathSegment!!)
            firebaseProfileImgStorageRef.putFile(localProfileImgUri).addOnFailureListener {
                Log.d("sammy", "fail = ${it.message.toString()}")
            }.addOnSuccessListener {
                firebaseProfileImgStorageRef.downloadUrl.addOnSuccessListener { uri ->
                    val profileUpdates = UserProfileChangeRequest.Builder().setPhotoUri(uri).build()
                    currentUser.updateProfile(profileUpdates)
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}

