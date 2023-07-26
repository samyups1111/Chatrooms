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
    val myRef = databaseFirebase.getReference("message")

    fun getMessagesRealtime(): Flow<String> = callbackFlow {
        val firebaseDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.value as String
                trySend(messages)
            }
            override fun onCancelled(error: DatabaseError) {
                trySend(error.message)
            }
        }
        myRef.addValueEventListener(firebaseDataListener)
        awaitClose {
            myRef.removeEventListener(firebaseDataListener)
        }
    }


}