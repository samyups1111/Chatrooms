package com.sample.mainapplication.ui.login

import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.sample.mainapplication.model.User
import com.sample.mainapplication.networking.LoginResult
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    val auth = Firebase.auth

    val user = flow<User> {
        val userRemote = auth.currentUser
        val name = userRemote?.displayName
        val id = userRemote?.uid
        emit(
            User(
                userId = id ?: "error",
                userName = name ?: "unknown",
        ))
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

    fun signOut() {
        auth.signOut()
    }
}

