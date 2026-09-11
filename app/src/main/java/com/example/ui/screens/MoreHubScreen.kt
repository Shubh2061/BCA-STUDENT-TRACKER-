package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.ui.theme.BrandPrimary
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun MoreHubScreen(
    viewModel: BcaViewModel,
    onNavigateToCodingHub: () -> Unit,
    onNavigateToLabTracker: () -> Unit,
    onNavigateToAssignments: () -> Unit,
    onNavigateToExams: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val activeSemester by viewModel.selectedSemester.collectAsState()

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = "BCA Student Hub",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "All Tools, Trackers & Semester Management",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Student Profile Header Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(18.dp),
                onClick = onNavigateToProfile,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = profile?.name?.ifBlank { "BCA Student" } ?: "BCA Student",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${profile?.college ?: "University"} • Sem $activeSemester",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                        Text(
                            text = "Target Attendance: ${profile?.requiredAttendancePercentage?.toInt() ?: 75}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }

            Text(
                text = "BCA Practical & Code Modules",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HubActionCard(
                title = "💻 Programming & Coding Hub",
                subtitle = "Practice C, Loops, Pointers, OOP & code snippets",
                badge = "BCA Core",
                icon = Icons.Default.Code,
                color = MaterialTheme.colorScheme.primary,
                onClick = onNavigateToCodingHub
            )

            HubActionCard(
                title = "🧪 Practical / Lab Tracker",
                subtitle = "Experiments, Lab file signatures & Viva preparation",
                badge = "Labs",
                icon = Icons.Default.Science,
                color = Color(0xFF0284C7),
                onClick = onNavigateToLabTracker
            )

            Text(
                text = "Academic Planners & Deadlines",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HubActionCard(
                title = "📝 Assignment Tracker",
                subtitle = "Track homework, worksheets & submission deadlines",
                badge = "Tasks",
                icon = Icons.Default.Assignment,
                color = Color(0xFFF59E0B),
                onClick = onNavigateToAssignments
            )

            HubActionCard(
                title = "📖 Exam Tracker & Countdown",
                subtitle = "Mid-sem, Sessional, Viva & Final exam schedules",
                badge = "Exams",
                icon = Icons.Default.Timer,
                color = Color(0xFF7C3AED),
                onClick = onNavigateToExams
            )

            HubActionCard(
                title = "📈 Semester Analytics & Readiness",
                subtitle = "Overall attendance, syllabus & academic health score",
                badge = "Analytics",
                icon = Icons.Default.BarChart,
                color = Color(0xFF10B981),
                onClick = onNavigateToProgress
            )

            HubActionCard(
                title = "📆 Academic Calendar",
                subtitle = "Monthly grid with classes, exams & assignment marks",
                badge = "Monthly",
                icon = Icons.Default.CalendarMonth,
                color = Color(0xFF06B6D4),
                onClick = onNavigateToCalendar
            )

            HubActionCard(
                title = "📑 Quick Notes & Formula Sheets",
                subtitle = "Algorithms, syntax cheatsheets & viva definitions",
                badge = "Notes",
                icon = Icons.Default.Notes,
                color = Color(0xFF8B5CF6),
                onClick = onNavigateToNotes
            )

            HubActionCard(
                title = "⚙️ Profile & Switch Semester",
                subtitle = "Switch between Sem 1 to 6 and adjust reminders",
                badge = "Settings",
                icon = Icons.Default.Settings,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                onClick = onNavigateToProfile
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun HubActionCard(
    title: String,
    subtitle: String,
    badge: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f))
        }
    }
}
