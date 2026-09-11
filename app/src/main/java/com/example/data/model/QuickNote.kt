package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quick_notes")
data class QuickNote(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semester: Int,
    val subjectId: Long? = null,
    val subjectName: String = "",
    val category: String = "Programming", // Programming, Mathematics, Computer Fundamentals, English, Labs, Exams, General
    val title: String,
    val content: String,
    val updatedAt: Long = System.currentTimeMillis()
)
