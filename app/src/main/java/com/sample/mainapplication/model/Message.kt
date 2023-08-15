package com.sample.mainapplication.model

import com.google.firebase.database.IgnoreExtraProperties
import java.text.SimpleDateFormat
import java.util.Date

@IgnoreExtraProperties
data class Message(
    val userName: String? = null,
    val text: String? = null,
    val date: Long? = null,
) {

    companion object {
        fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("h:mm a")
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
