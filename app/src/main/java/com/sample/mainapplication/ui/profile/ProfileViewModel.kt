package com.sample.mainapplication.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.ui.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
): ViewModel() {

    val userFlow = authRepository.user

    fun signOut() = authRepository.signOut()

    fun updateUser(name: String) = viewModelScope.launch { authRepository.updateUser(name) }
}