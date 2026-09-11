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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.ui.theme.BrandPrimary
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun ProfileScreen(
    viewModel: BcaViewModel,
    onBack: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val activeSemester by viewModel.selectedSemester.collectAsState()

    var name by remember(profile) { mutableStateOf(profile?.name ?: "") }
    var college by remember(profile) { mutableStateOf(profile?.college ?: "") }
    var year by remember(profile) { mutableStateOf(profile?.year ?: "1st Year") }
    var section by remember(profile) { mutableStateOf(profile?.section ?: "A") }
    var attendanceReq by remember(profile) { mutableFloatStateOf(profile?.requiredAttendancePercentage?.toFloat() ?: 75f) }
    var remindersEnabled by remember(profile) { mutableStateOf(profile?.notificationsEnabled ?: true) }
    var reminderMinutes by remember(profile) { mutableIntStateOf(profile?.reminderMinutesBefore ?: 15) }

    var showSaveToast by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                        Column {
                            Text(
                                text = "👤 Student Profile & Settings",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Manage BCA details, Semesters & Reminders",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Button(
                        onClick = {
                            val updated = (profile ?: StudentProfile(id = 1)).copy(
                                name = name.trim(),
                                college = college.trim(),
                                year = year,
                                currentSemester = activeSemester,
                                section = section.trim(),
                                requiredAttendancePercentage = attendanceReq.toDouble(),
                                notificationsEnabled = remindersEnabled,
                                reminderMinutesBefore = reminderMinutes
                            )
                            viewModel.saveProfile(updated)
                            showSaveToast = true
                        },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save")
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
            if (showSaveToast) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Text("Profile updated successfully!", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Semester Switcher Section (Section 1 & 14 in prompt: Switch between semesters 1-6 while keeping historical data safe)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "🎓 Switch BCA Semester",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Switch active view to any semester (1 to 6). Historical records for each semester remain securely saved.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (1..3).forEach { sem ->
                            FilterChip(
                                selected = activeSemester == sem,
                                onClick = { viewModel.switchSemester(sem) },
                                label = { Text("Sem $sem", fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (4..6).forEach { sem ->
                            FilterChip(
                                selected = activeSemester == sem,
                                onClick = { viewModel.switchSemester(sem) },
                                label = { Text("Sem $sem", fontWeight = FontWeight.Bold) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // Student Information Form
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Student Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Student Full Name") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = college,
                        onValueChange = { college = it },
                        label = { Text("College / University") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("BCA Year", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("1st Year", "2nd Year", "3rd Year").forEach { yr ->
                            FilterChip(
                                selected = year == yr,
                                onClick = { year = yr },
                                label = { Text(yr) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    OutlinedTextField(
                        value = section,
                        onValueChange = { section = it },
                        label = { Text("Section / Batch") },
                        placeholder = { Text("e.g. Section A / Batch 1") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Attendance Requirement Setting
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Attendance Requirement",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${attendanceReq.toInt()}%",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Slider(
                        value = attendanceReq,
                        onValueChange = { attendanceReq = it },
                        valueRange = 60f..95f,
                        steps = 6,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "Default university requirement is 75%. Safe / Critical calculations adjust automatically based on this value.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Class Notification Preferences
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Class Reminders", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Notify before upcoming classes", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(checked = remindersEnabled, onCheckedChange = { remindersEnabled = it })
                    }

                    if (remindersEnabled) {
                        Text("Reminder Timing", style = MaterialTheme.typography.labelMedium)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf(5, 10, 15, 30).forEach { mins ->
                                FilterChip(
                                    selected = reminderMinutes == mins,
                                    onClick = { reminderMinutes = mins },
                                    label = { Text("${mins} mins", style = MaterialTheme.typography.bodySmall) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Quick Data Actions
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Data & Curriculum", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Quickly pre-fill default subjects, syllabus topics, and sample timetable for Sem $activeSemester.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = { viewModel.seedSemesterIfEmpty(activeSemester) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Pre-fill Sem $activeSemester Curriculum")
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
