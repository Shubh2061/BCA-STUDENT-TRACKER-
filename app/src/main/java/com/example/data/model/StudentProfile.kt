package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "student_profile")
data class StudentProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "",
    val college: String = "",
    val year: String = "1st Year",
    val currentSemester: Int = 1,
    val section: String = "A",
    val requiredAttendancePercentage: Double = 75.0,
    val notificationsEnabled: Boolean = true,
    val reminderMinutesBefore: Int = 15,
    val isSetupCompleted: Boolean = false
)
