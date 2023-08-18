package com.sample.mainapplication.model

import android.net.Uri
import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.Date

@IgnoreExtraProperties
data class Message(
    val userId: String? = null,
    val userName: String? = null,
    val userImg: Uri? = null,
    val text: String? = null,
    val date: Long? = null,
) {

    companion object {
        fun convertLongToTime(time: Long): String {
            val time = Date(time)
            val format = SimpleDateFormat("h:mm a")
            return format.format(time)
        }

        fun convertLongToDate(date: Long): String {
            val date = Date(date)
            val format = SimpleDateFormat("MMM d, YYYY")
            return format.format(date)
        }

        fun convertDateToLong(date: String): Long {
            val df = SimpleDateFormat("h:mm a")
            return df.parse(date)?.time ?: 0
        }

        fun currentTimeToLong(): Long {
            return System.currentTimeMillis()
        }
    }

}
