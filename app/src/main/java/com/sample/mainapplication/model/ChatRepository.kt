package com.sample.mainapplication.model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ChatRepository @Inject constructor() {

    private val databaseFirebase = Firebase.database
    val messagesDatabase = databaseFirebase.getReference("messages")

    fun getMessagesRealtime(pokeName: String): Flow<List<Message>> = callbackFlow {
        val firebaseDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Message>()
                val data = snapshot.children

                data.forEach {
                    it.getValue(Message::class.java)?.let { it1 -> list.add(it1) }
                }
                trySend(list)
            }
            override fun onCancelled(error: DatabaseError) {
            }
        }
        messagesDatabase.child(pokeName).addValueEventListener(firebaseDataListener)
        awaitClose {
            messagesDatabase.removeEventListener(firebaseDataListener)
        }
    }

    fun writeNewMessage(
        messageId: String,
        name: String,
        message: String,
    ) {
        val message = Message(
            userName = name,
            text = message,
        )
        val key = messagesDatabase.push().key
        if (key != null) {
            messagesDatabase.child(messageId).child(key).setValue(message)
        }
    }


}