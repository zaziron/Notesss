package com.example.note.data

import android.graphics.Bitmap

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val image: Bitmap? = null,
    val timestamp: Long = System.currentTimeMillis()
)
