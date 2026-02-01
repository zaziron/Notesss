package com.example.note.model

import android.graphics.Bitmap

data class NoteItem(
    val id: Int,
    val title: String,
    val content: String,
    val image: Bitmap? = null
)
