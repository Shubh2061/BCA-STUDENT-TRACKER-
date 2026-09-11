package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.BcaViewModel

enum class MainTab(val title: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    ATTENDANCE("Attendance", Icons.Default.CheckCircle),
    SUBJECTS("Subjects", Icons.Default.MenuBook),
    TIMETABLE("Timetable", Icons.Default.Schedule),
    MORE("More", Icons.Default.GridView)
}

enum class SubScreen {
    NONE,
    CODING_HUB,
    LAB_TRACKER,
    ASSIGNMENTS,
    EXAMS,
    SEMESTER_PROGRESS,
    CALENDAR,
    NOTES,
    PROFILE
}

@Composable
fun MainAppScreen(
    viewModel: BcaViewModel
) {
    val profile by viewModel.profile.collectAsState()

    var selectedTab by remember { mutableStateOf(MainTab.HOME) }
    var currentSubScreen by remember { mutableStateOf(SubScreen.NONE) }

    // Intercept back button when on a sub-screen
    BackHandler(enabled = currentSubScreen != SubScreen.NONE) {
        currentSubScreen = SubScreen.NONE
    }

    if (profile == null || !profile!!.isSetupCompleted) {
        // First-Time User Experience Onboarding
        OnboardingScreen(
            onComplete = { studentProfile, usePresets ->
                if (usePresets) {
                    viewModel.setupWithPresets(studentProfile)
                } else {
                    viewModel.saveProfile(studentProfile)
                }
            }
        )
    } else {
        Scaffold(
            bottomBar = {
                if (currentSubScreen == SubScreen.NONE) {
                    NavigationBar(
                        tonalElevation = 8.dp,
                        modifier = Modifier.navigationBarsPadding()
                    ) {
                        MainTab.values().forEach { tab ->
                            val isSelected = selectedTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { selectedTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.title
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Handle SubScreens first
                when (currentSubScreen) {
                    SubScreen.CODING_HUB -> {
                        CodingPracticeScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.LAB_TRACKER -> {
                        LabTrackerScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.ASSIGNMENTS -> {
                        AssignmentsScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.EXAMS -> {
                        ExamsScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.SEMESTER_PROGRESS -> {
                        SemesterProgressScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.CALENDAR -> {
                        CalendarScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.NOTES -> {
                        NotesScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.PROFILE -> {
                        ProfileScreen(
                            viewModel = viewModel,
                            onBack = { currentSubScreen = SubScreen.NONE }
                        )
                    }
                    SubScreen.NONE -> {
                        // Main Bottom Bar Tabs
                        when (selectedTab) {
                            MainTab.HOME -> {
                                HomeScreen(
                                    viewModel = viewModel,
                                    onNavigateToAttendance = { selectedTab = MainTab.ATTENDANCE },
                                    onNavigateToTimetable = { selectedTab = MainTab.TIMETABLE },
                                    onNavigateToSyllabus = { selectedTab = MainTab.SUBJECTS },
                                    onNavigateToAssignments = { currentSubScreen = SubScreen.ASSIGNMENTS },
                                    onNavigateToExams = { currentSubScreen = SubScreen.EXAMS }
                                )
                            }
                            MainTab.ATTENDANCE -> {
                                AttendanceScreen(viewModel = viewModel)
                            }
                            MainTab.SUBJECTS -> {
                                SubjectsScreen(
                                    viewModel = viewModel,
                                    onNavigateToCodingHub = { currentSubScreen = SubScreen.CODING_HUB }
                                )
                            }
                            MainTab.TIMETABLE -> {
                                TimetableScreen(viewModel = viewModel)
                            }
                            MainTab.MORE -> {
                                MoreHubScreen(
                                    viewModel = viewModel,
                                    onNavigateToCodingHub = { currentSubScreen = SubScreen.CODING_HUB },
                                    onNavigateToLabTracker = { currentSubScreen = SubScreen.LAB_TRACKER },
                                    onNavigateToAssignments = { currentSubScreen = SubScreen.ASSIGNMENTS },
                                    onNavigateToExams = { currentSubScreen = SubScreen.EXAMS },
                                    onNavigateToProgress = { currentSubScreen = SubScreen.SEMESTER_PROGRESS },
                                    onNavigateToCalendar = { currentSubScreen = SubScreen.CALENDAR },
                                    onNavigateToNotes = { currentSubScreen = SubScreen.NOTES },
                                    onNavigateToProfile = { currentSubScreen = SubScreen.PROFILE }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
