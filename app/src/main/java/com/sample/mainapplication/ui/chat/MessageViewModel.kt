package com.sample.mainapplication.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.mainapplication.model.MessageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(
    private val messageRepository: MessageRepository,
): ViewModel() {

    fun getMessagesFlow(pokeName: String) = messageRepository.getMessagesRealtime(pokeName)

    fun writeNewMessage(
        chatroomName: String,
        message: String,
    ) = viewModelScope.launch { messageRepository.writeNewMessage(chatroomName, message) }
}