package com.sample.mainapplication.ui.chat

import androidx.lifecycle.ViewModel
import com.sample.mainapplication.model.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
): ViewModel() {

    fun setMessage(message: String?) = message.let { chatRepository.myRef.setValue(it) }
    fun getMessagesFlow() = chatRepository.getMessagesRealtime()
}