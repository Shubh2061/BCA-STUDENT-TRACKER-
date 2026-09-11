package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AttendanceCalculator
import com.example.data.model.BcaSubject
import com.example.ui.components.AttendanceSimulatorDialog
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun AttendanceScreen(
    viewModel: BcaViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val currentSemester by viewModel.selectedSemester.collectAsState()
    val overallAttendance by viewModel.overallAttendancePct.collectAsState()

    val reqPercentage = profile?.requiredAttendancePercentage ?: 75.0

    var filterMode by remember { mutableStateOf("ALL") } // ALL, SAFE, CRITICAL
    var selectedSubjectForSimulator by remember { mutableStateOf<BcaSubject?>(null) }

    val filteredSubjects = remember(subjects, filterMode, reqPercentage) {
        when (filterMode) {
            "SAFE" -> subjects.filter { it.attendancePercentage >= reqPercentage }
            "CRITICAL" -> subjects.filter { it.attendancePercentage < reqPercentage }
            else -> subjects
        }
    }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Attendance Tracker",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Sem $currentSemester",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Overall Summary Banner
                    val isOverallSafe = overallAttendance >= reqPercentage
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isOverallSafe) SuccessGreen.copy(alpha = 0.12f) else DangerRed.copy(alpha = 0.12f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Overall Attendance",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = if (isOverallSafe) "🟢 Attendance is Safe" else "🔴 Attendance is Below ${reqPercentage.toInt()}%",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isOverallSafe) SuccessGreen else DangerRed
                                )
                            }
                            Text(
                                text = "%.1f%%".format(overallAttendance),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isOverallSafe) SuccessGreen else DangerRed
                            )
                        }
                    }

                    // Filter Chips
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = filterMode == "ALL",
                            onClick = { filterMode = "ALL" },
                            label = { Text("All (${subjects.size})") }
                        )
                        FilterChip(
                            selected = filterMode == "SAFE",
                            onClick = { filterMode = "SAFE" },
                            label = { Text("Safe (${subjects.count { it.attendancePercentage >= reqPercentage }})") }
                        )
                        FilterChip(
                            selected = filterMode == "CRITICAL",
                            onClick = { filterMode = "CRITICAL" },
                            label = { Text("Critical (${subjects.count { it.attendancePercentage < reqPercentage }})") }
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (subjects.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EventBusy,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "No subjects added for Semester $currentSemester",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Add subjects in the Subjects tab or pre-fill standard BCA curriculum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(onClick = { viewModel.seedSemesterIfEmpty(currentSemester) }) {
                        Text("Pre-fill BCA Sem $currentSemester Subjects")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredSubjects, key = { it.id }) { subject ->
                    SubjectAttendanceCard(
                        subject = subject,
                        requiredPercentage = reqPercentage,
                        onPresent = { viewModel.markAttendance(subject, true) },
                        onAbsent = { viewModel.markAttendance(subject, false) },
                        onOpenSimulator = { selectedSubjectForSimulator = subject }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(36.dp))
                }
            }
        }
    }

    selectedSubjectForSimulator?.let { sub ->
        AttendanceSimulatorDialog(
            subject = sub,
            requiredPct = reqPercentage,
            onDismiss = { selectedSubjectForSimulator = null },
            onUpdateCounts = { present, absent ->
                viewModel.updateSubjectAttendance(sub.id, present, absent)
            }
        )
    }
}

@Composable
private fun SubjectAttendanceCard(
    subject: BcaSubject,
    requiredPercentage: Double,
    onPresent: () -> Unit,
    onAbsent: () -> Unit,
    onOpenSimulator: () -> Unit
) {
    val analysis = remember(subject.presentClasses, subject.absentClasses, requiredPercentage) {
        AttendanceCalculator.calculate(
            present = subject.presentClasses,
            absent = subject.absentClasses,
            targetPercentage = requiredPercentage
        )
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Subject Name, Code & Attendance %
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${subject.code.ifBlank { "BCA" }} • ${if (subject.isPractical) "Practical Lab" else "Theory"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%.1f%%".format(analysis.currentPercentage),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (analysis.isSafe) SuccessGreen else DangerRed
                    )
                    Text(
                        text = if (analysis.isSafe) "SAFE" else "CRITICAL",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (analysis.isSafe) SuccessGreen else DangerRed
                    )
                }
            }

            // Stats breakdown: Present / Absent / Total
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Present: ${subject.presentClasses}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SuccessGreen
                )
                Text(
                    text = "Absent: ${subject.absentClasses}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = DangerRed
                )
                Text(
                    text = "Total: ${subject.totalClasses}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Progress Bar
            LinearProgressIndicator(
                progress = { (analysis.currentPercentage / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (analysis.isSafe) SuccessGreen else DangerRed,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            // Smart Calculator Guidance Message
            Surface(
                color = if (analysis.isSafe) SuccessGreen.copy(alpha = 0.08f) else DangerRed.copy(alpha = 0.08f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (analysis.isSafe) "🟢" else "🔴",
                        fontSize = 14.sp
                    )
                    Text(
                        text = analysis.statusMessage,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Buttons: Present, Absent, and Smart Simulator button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onPresent,
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("✅ Present")
                }

                Button(
                    onClick = onAbsent,
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("❌ Absent")
                }

                OutlinedIconButton(
                    onClick = onOpenSimulator,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Smart Calculator & Simulator",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
