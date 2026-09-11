package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class ExamEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semester: Int,
    val subjectId: Long,
    val subjectName: String,
    val examType: String = "Mid-Sem", // Internal, Mid-Sem, Practical, End-Sem, Viva
    val date: String, // "YYYY-MM-DD"
    val time: String = "10:00 AM",
    val room: String = "Room 204"
)
