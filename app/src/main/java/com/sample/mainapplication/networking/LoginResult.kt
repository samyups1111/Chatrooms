package com.sample.mainapplication.networking

sealed class LoginResult {

    data class MISSING_USERNAME(
        val text: String = "Username cannot be blank"
    ): LoginResult()

    data class MISSING_PASSWORD(
        val text: String = "Password cannot be blank"
    ): LoginResult()

    data class ERROR(
        val text: String = "Incorrect Username or Password"
    ): LoginResult()

    object SUCCESS: LoginResult()
}