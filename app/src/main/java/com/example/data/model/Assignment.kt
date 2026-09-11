package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semester: Int,
    val subjectId: Long,
    val subjectName: String,
    val title: String,
    val description: String = "",
    val dueDate: String, // "YYYY-MM-DD"
    val priority: String = "MEDIUM", // HIGH, MEDIUM, LOW
    val isCompleted: Boolean = false
)
