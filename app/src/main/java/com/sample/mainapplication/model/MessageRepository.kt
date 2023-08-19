package com.sample.mainapplication.model

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageRepository @Inject constructor(
    private val user: Flow<User>,
) {
    private val messagesDatabaseRef = Firebase.database.getReference("messages")
    private val firebaseStorageRef = Firebase.storage.reference

    fun getMessagesRealtime(pokeName: String): Flow<List<Message>> = callbackFlow {
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
        messagesDatabaseRef.child(pokeName).addValueEventListener(firebaseDataListener)
        awaitClose {
            messagesDatabaseRef.removeEventListener(firebaseDataListener)
        }
    }

    suspend fun writeNewMessage(
        messageId: String,
        message: String,
    ) {
        user.collect {
            val profileImgUri = firebaseStorageRef.child("images").child(it.userId).child("profile_image").downloadUrl.await()
            val message = Message(
                userId = it.userId,
                userName = it.userName,
                text = message,
                date = Message.currentTimeToLong(),
                userImgUri = profileImgUri.toString(),
            )
            val key = messagesDatabaseRef.push().key
            if (key != null) {
                messagesDatabaseRef.child(messageId).child(key).setValue(message)
            }
        }
    }
}