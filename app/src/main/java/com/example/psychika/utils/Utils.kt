package com.example.psychika.utils

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Utils {
    private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
    private val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(Date())
    private val CHAT_TIME_FORMAT = "HH:mm"

    fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    fun getCurrentTime(): String {
        val currentTime = Calendar.getInstance().time
        return SimpleDateFormat(CHAT_TIME_FORMAT, Locale.getDefault()).format(currentTime)
    }
}