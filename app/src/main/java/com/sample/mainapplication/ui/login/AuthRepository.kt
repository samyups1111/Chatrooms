package com.sample.mainapplication.ui.login

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sample.mainapplication.networking.LoginResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    private val auth = Firebase.auth

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
}

