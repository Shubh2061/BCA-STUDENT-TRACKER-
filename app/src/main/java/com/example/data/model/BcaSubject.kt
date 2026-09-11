package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bca_subjects")
data class BcaSubject(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val semester: Int,
    val name: String,
    val code: String = "",
    val teacher: String = "",
    val room: String = "",
    val isPractical: Boolean = false,
    val isProgramming: Boolean = false,
    val credits: Int = 4,
    val examDate: String = "",
    val notes: String = "",
    val colorHex: Long = 0xFF2563EB,
    val presentClasses: Int = 0,
    val absentClasses: Int = 0
) {
    val totalClasses: Int get() = presentClasses + absentClasses
    val attendancePercentage: Double
        get() = if (totalClasses == 0) 100.0 else (presentClasses.toDouble() / totalClasses) * 100.0
}
