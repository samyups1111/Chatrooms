package com.sample.mainapplication.model

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatroomRepository @Inject constructor(

) {
    private val chatroomDatabaseRef = Firebase.database.getReference("chatrooms")

    fun getChatroomsRealTime(): Flow<List<String>> = callbackFlow {
        val firebaseDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                val data = snapshot.children

                data.forEach {
                    val chatRoomName = it.key
                    list.add(chatRoomName!!)
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        chatroomDatabaseRef.addValueEventListener(firebaseDataListener)
        awaitClose {
            chatroomDatabaseRef.removeEventListener(firebaseDataListener)
        }
    }
    // todo: NOt doing anything. Is it because it has no child?
    fun createNewChatroom(name: String) {
        chatroomDatabaseRef.child(name).push()
    }
}