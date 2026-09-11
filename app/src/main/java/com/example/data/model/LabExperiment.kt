package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lab_experiments")
data class LabExperiment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val subjectId: Long,
    val subjectName: String = "",
    val experimentNumber: Int,
    val title: String,
    val date: String = "",
    val isCompleted: Boolean = false,
    val fileStatus: String = "Pending", // Pending, In Progress, Signed & Checked
    val vivaStatus: String = "Not Prepared", // Not Prepared, Needs Revision, Prepared
    val codeOrObservations: String = ""
)
