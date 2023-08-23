package com.sample.mainapplication.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.AuthRepository
import com.sample.mainapplication.networking.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    private val _registrationResult = MutableLiveData<LoginResult>()
    val registrationResult : LiveData<LoginResult> = _registrationResult

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult : LiveData<LoginResult> = _loginResult

    fun onRegister(
        email: String?,
        password: String?,
    ) {
        viewModelScope.launch {
            _registrationResult.value =
                if (email == null || email == "") LoginResult.MISSING_USERNAME()
                else if (password == null || password == "") LoginResult.MISSING_PASSWORD()
                else authRepository.createUser(email, password)
        }
    }

    fun onLogin(
        email: String?,
        password: String?,
    ) {
        viewModelScope.launch {
            _loginResult.value =
                if (email == null || email == "") LoginResult.MISSING_USERNAME()
                else if (password == null || password == "") LoginResult.MISSING_PASSWORD()
                else authRepository.login(email, password)
        }
    }
}

