package com.example.rickandmorty.ui.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun String.toFormattedDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        Log.e("DateFormatter", "Failed to parse date string: '$this'", e)
        this
    }
}