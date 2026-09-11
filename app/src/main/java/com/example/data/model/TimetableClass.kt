package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable_classes")
data class TimetableClass(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semester: Int,
    val dayOfWeek: Int, // 1 = Monday, 2 = Tuesday, ... 7 = Sunday
    val subjectId: Long,
    val subjectName: String,
    val teacher: String = "",
    val room: String = "",
    val startTime: String, // "10:00" (24h format HH:mm)
    val endTime: String,   // "11:00"
    val isPractical: Boolean = false
)
