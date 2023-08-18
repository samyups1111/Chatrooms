package com.sample.mainapplication.ui.profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.AuthRepository
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

    fun savelocalProfileImgUriToFirebase(file: Uri) = authRepository.saveLocalProfileImgUriToFirebase(file)
}