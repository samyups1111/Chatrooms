package com.sample.mainapplication.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val user: Flow<User>,
    private val messagesDatabaseRef: DatabaseReference,
) {
    fun getMessagesRealtime(chatroomName: String): Flow<List<Message>> = callbackFlow {
        val firebaseDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Message>()
                val data = snapshot.children

                data.forEach {
                    it.getValue(Message::class.java)?.let { message ->
                        list.add(message)
                    }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        messagesDatabaseRef.child(chatroomName).addValueEventListener(firebaseDataListener)
        awaitClose {
            messagesDatabaseRef.removeEventListener(firebaseDataListener)
        }
    }

    suspend fun writeNewMessage(
        chatroomName: String,
        text: String,
    ) {
        user.collect { currentUser ->
            val message = Message(
                userId = currentUser.userId,
                userName = currentUser.userName,
                text = text,
                date = Message.currentTimeToLong(),
                userImgUri = currentUser.photoUrl,
            )
            val key = messagesDatabaseRef.push().key
            if (key != null) {
                messagesDatabaseRef.child(chatroomName).child(key).setValue(message)
            }
        }
    }
}