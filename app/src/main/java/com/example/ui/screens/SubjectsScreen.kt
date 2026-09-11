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
import com.example.data.model.BcaSubject
import com.example.data.model.SyllabusTopic
import com.example.ui.components.AddSubjectDialog
import com.example.ui.components.AddTopicDialog
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun SubjectsScreen(
    viewModel: BcaViewModel,
    onNavigateToCodingHub: () -> Unit
) {
    val currentSemester by viewModel.selectedSemester.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val allTopics by viewModel.syllabusTopics.collectAsState()
    val overallSyllabus by viewModel.overallSyllabusPct.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Subjects List, 1 = Syllabus Tracker
    var showAddSubjectDialog by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<BcaSubject?>(null) }
    var addingTopicForSubjectId by remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "BCA Subjects & Syllabus",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Semester $currentSemester • ${subjects.size} Subjects",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Button(
                            onClick = { showAddSubjectDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Subject")
                        }
                    }

                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = { Text("Subjects (${subjects.size})", fontWeight = FontWeight.Bold) }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "Syllabus (%.0f%%)".format(overallSyllabus),
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.padding(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "No subjects added for Sem $currentSemester",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Add custom subjects or auto-populate standard BCA curriculum.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { viewModel.seedSemesterIfEmpty(currentSemester) }) {
                            Text("Pre-fill BCA Subjects")
                        }
                        OutlinedButton(onClick = { showAddSubjectDialog = true }) {
                            Text("Add Custom")
                        }
                    }
                }
            }
        } else {
            AnimatedContent(
                targetState = selectedTab,
                label = "subjects_tab_content",
                modifier = Modifier.padding(padding)
            ) { tabIndex ->
                if (tabIndex == 0) {
                    // Subjects List Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(subjects, key = { it.id }) { subject ->
                            SubjectCard(
                                subject = subject,
                                onEdit = { editingSubject = subject },
                                onDelete = { viewModel.deleteSubject(subject) },
                                onOpenCoding = onNavigateToCodingHub
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                } else {
                    // Syllabus Tracker Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Overall Syllabus Progress Banner
                        item {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Overall Semester Syllabus",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                        Text(
                                            text = "%.0f%% Completed".format(overallSyllabus),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    LinearProgressIndicator(
                                        progress = { (overallSyllabus / 100.0).toFloat().coerceIn(0f, 1f) },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                    Text(
                                        text = "Status: ⚪ Not Started  •  🟡 In Progress  •  🟢 Completed",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }

                        // For each subject, display units & topics
                        items(subjects, key = { "syllabus_${it.id}" }) { subject ->
                            val subjectTopics = allTopics.filter { it.subjectId == subject.id }
                            SubjectSyllabusSection(
                                subject = subject,
                                topics = subjectTopics,
                                onAddTopic = { addingTopicForSubjectId = subject.id },
                                onUpdateStatus = { topicId, newStatus ->
                                    viewModel.updateTopicStatus(topicId, newStatus)
                                },
                                onDeleteTopic = { topic ->
                                    viewModel.deleteTopic(topic)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(40.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddSubjectDialog) {
        AddSubjectDialog(
            currentSemester = currentSemester,
            onDismiss = { showAddSubjectDialog = false },
            onSave = {
                viewModel.addSubject(it)
                showAddSubjectDialog = false
            }
        )
    }

    editingSubject?.let { sub ->
        AddSubjectDialog(
            currentSemester = currentSemester,
            subjectToEdit = sub,
            onDismiss = { editingSubject = null },
            onSave = {
                viewModel.updateSubject(it)
                editingSubject = null
            }
        )
    }

    addingTopicForSubjectId?.let { subId ->
        val existingUnits = allTopics.filter { it.subjectId == subId }.map { it.unitNumber }.distinct()
        AddTopicDialog(
            subjectId = subId,
            semester = currentSemester,
            existingUnits = existingUnits,
            onDismiss = { addingTopicForSubjectId = null },
            onSave = {
                viewModel.addTopic(it)
                addingTopicForSubjectId = null
            }
        )
    }
}

@Composable
private fun SubjectCard(
    subject: BcaSubject,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onOpenCoding: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
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
                        text = "${subject.code.ifBlank { "BCA" }} • Credits: ${subject.credits}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    AssistChip(
                        onClick = {},
                        label = { Text(if (subject.isPractical) "🧪 Lab" else "📖 Theory") }
                    )
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null
                        )
                    }
                }
            }

            // Quick Info Chips: Teacher & Room
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (subject.teacher.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(subject.teacher, style = MaterialTheme.typography.bodySmall)
                    }
                }
                if (subject.room.isNotBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Room, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Text(subject.room, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Programming Badge
            if (subject.isProgramming) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.clickable(onClick = onOpenCoding)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("💻 Coding Subject — View Practice Programs & Snippets →", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }

            if (expanded) {
                HorizontalDivider()
                if (subject.examDate.isNotBlank()) {
                    Text("Exam Date: ${subject.examDate}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                }
                if (subject.notes.isNotBlank()) {
                    Text("Notes: ${subject.notes}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Edit")
                    }
                    TextButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun SubjectSyllabusSection(
    subject: BcaSubject,
    topics: List<SyllabusTopic>,
    onAddTopic: () -> Unit,
    onUpdateStatus: (topicId: Long, newStatus: String) -> Unit,
    onDeleteTopic: (SyllabusTopic) -> Unit
) {
    var isExpanded by remember { mutableStateOf(true) }

    val completed = topics.count { it.status == "COMPLETED" }
    val inProgress = topics.count { it.status == "IN_PROGRESS" }
    val progressPct = if (topics.isEmpty()) 0.0 else ((completed + inProgress * 0.5) / topics.size) * 100.0

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subject.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${completed}/${topics.size} Topics Completed • %.0f%%".format(progressPct),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onAddTopic) {
                        Icon(Icons.Default.AddCircleOutline, contentDescription = "Add Topic", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = { isExpanded = !isExpanded }) {
                        Icon(if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
                    }
                }
            }

            LinearProgressIndicator(
                progress = { (progressPct / 100.0).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = SuccessGreen,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            if (isExpanded) {
                if (topics.isEmpty()) {
                    Text(
                        text = "No syllabus topics added yet. Tap '+' to add Unit topics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                } else {
                    // Group by unitNumber
                    val units = topics.groupBy { it.unitNumber }
                    units.toSortedMap().forEach { (unitNum, unitTopics) ->
                        val unitTitle = unitTopics.firstOrNull()?.unitTitle?.ifBlank { "Unit $unitNum" } ?: "Unit $unitNum"
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "📚 $unitTitle",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            unitTopics.forEach { topic ->
                                TopicRow(
                                    topic = topic,
                                    onUpdateStatus = { onUpdateStatus(topic.id, it) },
                                    onDelete = { onDeleteTopic(topic) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicRow(
    topic: SyllabusTopic,
    onUpdateStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = topic.topicTitle,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Status Pills: ⚪ Not Started, 🟡 In Progress, 🟢 Completed
            StatusIconButton(
                isSelected = topic.status == "NOT_STARTED",
                label = "⚪",
                onClick = { onUpdateStatus("NOT_STARTED") }
            )
            StatusIconButton(
                isSelected = topic.status == "IN_PROGRESS",
                label = "🟡",
                onClick = { onUpdateStatus("IN_PROGRESS") }
            )
            StatusIconButton(
                isSelected = topic.status == "COMPLETED",
                label = "🟢",
                onClick = { onUpdateStatus("COMPLETED") }
            )

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Delete Topic",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatusIconButton(
    isSelected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(30.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, fontSize = 13.sp)
    }
}
