package com.sample.mainapplication.ui.chatroom

import androidx.lifecycle.ViewModel
import com.sample.mainapplication.model.ChatroomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatroomViewModel @Inject constructor(
    private val chatroomRepository: ChatroomRepository,
): ViewModel() {

    fun getChatroomsRealTime() = chatroomRepository.getChatroomsRealTime()

    fun createNewChatroom(name: String) = chatroomRepository.createNewChatroom(name)
}