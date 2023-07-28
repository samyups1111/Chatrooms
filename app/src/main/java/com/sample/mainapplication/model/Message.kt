package com.sample.mainapplication.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Message(
    val userName: String? = null,
    val text: String? = null,
)
