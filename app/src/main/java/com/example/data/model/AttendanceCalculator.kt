package com.example.data.model

import kotlin.math.ceil
import kotlin.math.floor

data class AttendanceAnalysis(
    val present: Int,
    val absent: Int,
    val total: Int,
    val currentPercentage: Double,
    val requiredPercentage: Double,
    val isSafe: Boolean,
    val missableClasses: Int,
    val classesNeededForTarget: Int,
    val classesNeededFor80: Int,
    val statusMessage: String
)

object AttendanceCalculator {

    fun calculate(
        present: Int,
        absent: Int,
        targetPercentage: Double = 75.0
    ): AttendanceAnalysis {
        val total = present + absent
        val currentPct = if (total == 0) 100.0 else (present.toDouble() / total) * 100.0
        val targetRatio = targetPercentage / 100.0
        val target80Ratio = 0.80

        val isSafe = currentPct >= targetPercentage

        // Classes that can be missed while remaining >= targetPercentage:
        // We want: present / (total + m) >= targetRatio
        // <=> present >= targetRatio * total + targetRatio * m
        // <=> targetRatio * m <= present - targetRatio * total
        // <=> m <= (present - targetRatio * total) / targetRatio = (present / targetRatio) - total
        val missable = if (targetRatio <= 0.0 || !isSafe) {
            0
        } else {
            val maxM = floor((present.toDouble() / targetRatio) - total).toInt()
            if (maxM < 0) 0 else maxM
        }

        // Classes required to reach target without missing any:
        // (present + a) / (total + a) >= targetRatio
        // <=> present + a >= targetRatio * total + targetRatio * a
        // <=> a * (1 - targetRatio) >= targetRatio * total - present
        // <=> a >= (targetRatio * total - present) / (1 - targetRatio)
        val neededTarget = if (isSafe || targetRatio >= 1.0) {
            0
        } else {
            val requiredA = ceil((targetRatio * total - present) / (1.0 - targetRatio)).toInt()
            if (requiredA < 0) 0 else requiredA
        }

        // Classes needed for 80%:
        val needed80 = if (currentPct >= 80.0) {
            0
        } else {
            val required80 = ceil((target80Ratio * total - present) / (1.0 - target80Ratio)).toInt()
            if (required80 < 0) 0 else required80
        }

        val message = if (total == 0) {
            "No classes marked yet. Ready to record attendance!"
        } else if (isSafe) {
            if (missable > 0) {
                "Attendance is Safe! You can miss up to $missable more ${if (missable == 1) "class" else "classes"}."
            } else {
                "Attendance is exactly at or near ${targetPercentage.toInt()}%. Do not miss the next class!"
            }
        } else {
            "Attendance is below ${targetPercentage.toInt()}%. Attend the next $neededTarget ${if (neededTarget == 1) "class" else "classes"} without missing any to reach ${targetPercentage.toInt()}%."
        }

        return AttendanceAnalysis(
            present = present,
            absent = absent,
            total = total,
            currentPercentage = currentPct,
            requiredPercentage = targetPercentage,
            isSafe = isSafe,
            missableClasses = missable,
            classesNeededForTarget = neededTarget,
            classesNeededFor80 = needed80,
            statusMessage = message
        )
    }

    /**
     * Simulate future attendance: "What if I attend [futureAttended] and miss [futureMissed]?"
     */
    fun simulate(
        present: Int,
        absent: Int,
        futureAttended: Int,
        futureMissed: Int
    ): Double {
        val newPresent = present + futureAttended
        val newTotal = (present + absent) + futureAttended + futureMissed
        return if (newTotal == 0) 100.0 else (newPresent.toDouble() / newTotal) * 100.0
    }
}
