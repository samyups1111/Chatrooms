package com.sample.mainapplication.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
): ViewModel() {

    fun getMessagesFlow(pokeName: String) = chatRepository.getMessagesRealtime(pokeName)

    fun writeNewMessage(
        messageId: String,
        message: String,
    ) = viewModelScope.launch { chatRepository.writeNewMessage(messageId, message) }
}