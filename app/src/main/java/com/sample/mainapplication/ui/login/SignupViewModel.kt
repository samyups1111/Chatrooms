package com.sample.mainapplication.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.networking.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _state = MutableLiveData<LoginResult>()
    val state : LiveData<LoginResult> = _state

    fun onSignup(
        name: String?,
    ) {
        if (name.isNullOrBlank()) return

        viewModelScope.launch {
            val result = authRepository.updateUser(name)
            _state.value = result
        }
    }
}