package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coding_exercises")
data class CodingExercise(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val topicCategory: String, // Variables, Data Types, Operators, If-Else, Loops, Arrays, Functions, Pointers, Structures, etc.
    val title: String,
    val description: String = "",
    val difficulty: String = "Medium",
    val isCompleted: Boolean = false,
    val codeSnippet: String = "",
    val notesOrSnippet: String = ""
)
