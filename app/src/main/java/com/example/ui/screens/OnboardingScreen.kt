package com.example.ui.screens

import androidx.compose.animation.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.StudentProfile
import com.example.data.prepopulate.BcaPresetData
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.SuccessGreen

@Composable
fun OnboardingScreen(
    onComplete: (StudentProfile, usePresets: Boolean) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("1st Year") }
    var semester by remember { mutableIntStateOf(1) }
    var section by remember { mutableStateOf("A") }
    var attendanceReq by remember { mutableFloatStateOf(75f) }
    var usePresets by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "BCA Student Setup",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Step $step of 8",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { step / 8f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        },
        bottomBar = {
            Surface(
                tonalElevation = 6.dp,
                shadowElevation = 8.dp,
                modifier = Modifier.navigationBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 1) {
                        OutlinedButton(
                            onClick = { step-- },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Back")
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (step < 8) {
                                step++
                            } else {
                                val profile = StudentProfile(
                                    id = 1,
                                    name = if (name.isBlank()) "BCA Student" else name.trim(),
                                    college = if (college.isBlank()) "College of Computer Applications" else college.trim(),
                                    year = year,
                                    currentSemester = semester,
                                    section = section.ifBlank { "A" },
                                    requiredAttendancePercentage = attendanceReq.toDouble(),
                                    notificationsEnabled = true,
                                    reminderMinutesBefore = 15,
                                    isSetupCompleted = true
                                )
                                onComplete(profile, usePresets)
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        enabled = when (step) {
                            1 -> name.isNotBlank()
                            2 -> college.isNotBlank()
                            else -> true
                        }
                    ) {
                        Text(if (step == 8) "Launch Planner 🚀" else "Next Step →")
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            AnimatedContent(
                targetState = step,
                label = "onboarding_step"
            ) { targetStep ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    when (targetStep) {
                        1 -> Step1Name(name = name, onNameChange = { name = it })
                        2 -> Step2College(college = college, onCollegeChange = { college = it })
                        3 -> Step3Year(selectedYear = year, onYearSelected = { year = it })
                        4 -> Step4Semester(
                            selectedSemester = semester,
                            onSemesterSelected = { semester = it }
                        )
                        5 -> Step5Attendance(
                            attendance = attendanceReq,
                            onAttendanceChange = { attendanceReq = it }
                        )
                        6 -> Step6Subjects(
                            semester = semester,
                            usePresets = usePresets,
                            onTogglePresets = { usePresets = it }
                        )
                        7 -> Step7Timetable(usePresets = usePresets)
                        8 -> Step8Syllabus(usePresets = usePresets)
                    }
                }
            }
        }
    }
}

@Composable
private fun StepHeader(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Step1Name(name: String, onNameChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.Person,
            title = "What's your name?",
            subtitle = "Your daily college planner will be personalized for you."
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Student Full Name") },
            placeholder = { Text("e.g. Rohan Sharma") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Step2College(college: String, onCollegeChange: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.School,
            title = "Your College or University",
            subtitle = "Enter your BCA department / university name."
        )
        OutlinedTextField(
            value = college,
            onValueChange = onCollegeChange,
            label = { Text("College / University Name") },
            placeholder = { Text("e.g. Delhi University / BCA Institute") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Step3Year(selectedYear: String, onYearSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.DateRange,
            title = "Which BCA Year are you in?",
            subtitle = "Select your current academic year."
        )
        listOf("1st Year", "2nd Year", "3rd Year").forEach { yr ->
            Card(
                onClick = { onYearSelected(yr) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedYear == yr)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = yr,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    RadioButton(
                        selected = selectedYear == yr,
                        onClick = { onYearSelected(yr) }
                    )
                }
            }
        }
    }
}

@Composable
private fun Step4Semester(selectedSemester: Int, onSemesterSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.Layers,
            title = "Current Semester",
            subtitle = "BCA spans 6 semesters. You can switch semesters anytime."
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (1..3).forEach { sem ->
                FilterChip(
                    selected = selectedSemester == sem,
                    onClick = { onSemesterSelected(sem) },
                    label = { Text("Sem $sem", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            (4..6).forEach { sem ->
                FilterChip(
                    selected = selectedSemester == sem,
                    onClick = { onSemesterSelected(sem) },
                    label = { Text("Sem $sem", fontWeight = FontWeight.Bold) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun Step5Attendance(attendance: Float, onAttendanceChange: (Float) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.CheckCircle,
            title = "Attendance Target",
            subtitle = "Set your university's minimum mandatory attendance requirement."
        )
        Text(
            text = "${attendance.toInt()}%",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Slider(
            value = attendance,
            onValueChange = onAttendanceChange,
            valueRange = 60f..95f,
            steps = 6,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Standard BCA university requirement is 75%. We'll alert you whenever any subject drops below this.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun Step6Subjects(semester: Int, usePresets: Boolean, onTogglePresets: (Boolean) -> Unit) {
    val suggested = remember(semester) { BcaPresetData.getSuggestedSubjectsForSemester(semester) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.MenuBook,
            title = "BCA Subjects (Sem $semester)",
            subtitle = "We have prepared standard curriculum subjects for BCA Semester $semester."
        )

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Auto-load BCA Subjects", fontWeight = FontWeight.Bold)
                    Switch(checked = usePresets, onCheckedChange = onTogglePresets)
                }
                Text(
                    text = "Subjects vary across universities. You can easily add, edit or delete any subject later!",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider()
                suggested.forEach { sub ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${sub.name}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        AssistChip(
                            onClick = {},
                            label = { Text(if (sub.isPractical) "Lab" else "Theory") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step7Timetable(usePresets: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.Schedule,
            title = "Weekly Timetable",
            subtitle = "Timetable will be organized Monday to Sunday with class rooms & start/end times."
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("🗓️ Smart Timetable Features:", fontWeight = FontWeight.Bold)
                Text("• Highlights upcoming classes with countdowns")
                Text("• 1-Tap Attendance prompt as soon as class finishes")
                Text("• Class reminders 5m, 10m, 15m or 30m before")
                Text("• Fast, inline class editor for every day of the week")
            }
        }
    }
}

@Composable
private fun Step8Syllabus(usePresets: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StepHeader(
            icon = Icons.Default.DoneAll,
            title = "Ready to Launch!",
            subtitle = "Everything is set up. Your BCA dashboard is generated."
        )
        Card(
            colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.12f)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("✨ What you have ready:", fontWeight = FontWeight.Bold, color = SuccessGreen)
                Text("✅ Attendance Tracker with Safe/Critical Calculator")
                Text("✅ BCA Syllabus Tracker (Unit 1 to 5, Topics ⚪🟡🟢)")
                Text("✅ Practical Lab Tracker with Viva prep & file status")
                Text("✅ Programming practice exercises & code notes")
                Text("✅ Timetable, Assignments, Exams & Semester Switcher")
            }
        }
    }
}
