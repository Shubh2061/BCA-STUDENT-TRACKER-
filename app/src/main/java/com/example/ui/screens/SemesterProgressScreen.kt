package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun SemesterProgressScreen(
    viewModel: BcaViewModel,
    onBack: () -> Unit
) {
    val currentSemester by viewModel.selectedSemester.collectAsState()
    val attendancePct by viewModel.overallAttendancePct.collectAsState()
    val syllabusPct by viewModel.overallSyllabusPct.collectAsState()
    val assignmentsPct by viewModel.assignmentsCompletionPct.collectAsState()
    val labsPct by viewModel.labsCompletionPct.collectAsState()
    val profile by viewModel.profile.collectAsState()

    val reqAttendance = profile?.requiredAttendancePercentage ?: 75.0

    // Overall Readiness Score (weighted combination)
    val overallHealthScore = remember(attendancePct, syllabusPct, assignmentsPct, labsPct) {
        (attendancePct * 0.35) + (syllabusPct * 0.35) + (assignmentsPct * 0.15) + (labsPct * 0.15)
    }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(
                            text = "📈 Semester $currentSemester Progress",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Comprehensive Academic Health & Analytics",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Overall Score Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "BCA Readiness Index",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "%.0f%%".format(overallHealthScore),
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = when {
                            overallHealthScore >= 80 -> "🌟 Outstanding performance! You are on track for top grades."
                            overallHealthScore >= 65 -> "👍 Good standing! Focus on completing remaining syllabus & assignments."
                            else -> "⚠️ Needs attention. Prioritize attendance and completing pending lab work."
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Key Pillars Grid
            Text(
                text = "Performance Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            PillarProgressRow(
                title = "Class Attendance",
                percentage = attendancePct,
                targetText = "Target: ≥${reqAttendance.toInt()}%",
                isSafe = attendancePct >= reqAttendance,
                icon = Icons.Default.CheckCircle
            )

            PillarProgressRow(
                title = "Syllabus Coverage",
                percentage = syllabusPct,
                targetText = "Units 1 to 5 Topics",
                isSafe = syllabusPct >= 60,
                icon = Icons.Default.MenuBook
            )

            PillarProgressRow(
                title = "Assignments Submitted",
                percentage = assignmentsPct,
                targetText = "Worksheets & Submissions",
                isSafe = assignmentsPct >= 50,
                icon = Icons.Default.AssignmentTurnedIn
            )

            PillarProgressRow(
                title = "Practicals & Lab Files",
                percentage = labsPct,
                targetText = "Experiments & Viva Prep",
                isSafe = labsPct >= 50,
                icon = Icons.Default.Science
            )

            // BCA Advice & Tips Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("💡 BCA Student Success Guide:", fontWeight = FontWeight.Bold)
                    Text("• Maintain 75%+ attendance to avoid semester condonation penalties.")
                    Text("• Practice at least 2 coding problems daily in C / Data Structures.")
                    Text("• Keep practical file index signed after each lab session.")
                    Text("• Prepare 5 standard Viva questions for each completed experiment.")
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PillarProgressRow(
    title: String,
    percentage: Double,
    targetText: String,
    isSafe: Boolean,
    icon: ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(icon, contentDescription = null, tint = if (isSafe) SuccessGreen else DangerRed, modifier = Modifier.size(20.dp))
                    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "%.1f%%".format(percentage),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSafe) SuccessGreen else DangerRed
                )
            }

            LinearProgressIndicator(
                progress = { (percentage / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = if (isSafe) SuccessGreen else DangerRed,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Text(
                text = targetText,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
