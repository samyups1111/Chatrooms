package com.sample.mainapplication.model

import android.net.Uri

data class User(
    val userId: String,
    val userName: String,
    val profileImgUri: Uri? = null,
)
