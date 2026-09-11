package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "syllabus_topics")
data class SyllabusTopic(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val semester: Int,
    val unitNumber: Int,
    val unitTitle: String,
    val topicTitle: String,
    val status: String = "NOT_STARTED", // NOT_STARTED, IN_PROGRESS, COMPLETED
    val sortOrder: Int = 0
)
