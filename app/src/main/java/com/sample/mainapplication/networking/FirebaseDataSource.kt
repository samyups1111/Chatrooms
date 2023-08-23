package com.sample.mainapplication.networking

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class FirebaseDataSource @Inject constructor() {
    val auth = Firebase.auth
    val userId: String? = auth.currentUser?.uid
    val imageStorageRef = userId?.let { Firebase.storage.reference.child("images").child(it) }
    val userDatabaseRef = userId?.let { Firebase.database.getReference("user").child(it) }
    val chatroomDatabaseRef = Firebase.database.getReference("chatrooms")
}