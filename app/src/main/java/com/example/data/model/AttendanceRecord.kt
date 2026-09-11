package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val date: String, // "YYYY-MM-DD"
    val status: String, // PRESENT, ABSENT, CANCELLED
    val timestamp: Long = System.currentTimeMillis()
)
