package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimetableClassDialog(
    currentSemester: Int,
    subjects: List<BcaSubject>,
    defaultDayOfWeek: Int = 1,
    onDismiss: () -> Unit,
    onSave: (TimetableClass) -> Unit
) {
    var dayOfWeek by remember { mutableIntStateOf(defaultDayOfWeek) }
    var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: 0L) }
    var teacher by remember { mutableStateOf(subjects.firstOrNull()?.teacher ?: "") }
    var room by remember { mutableStateOf(subjects.firstOrNull()?.room ?: "") }
    var startTime by remember { mutableStateOf("10:00") }
    var endTime by remember { mutableStateOf("11:00") }
    var isPractical by remember { mutableStateOf(subjects.firstOrNull()?.isPractical ?: false) }

    val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Class to Timetable") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Day selector
                Text("Select Day", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    days.forEachIndexed { index, name ->
                        val dayNum = index + 1
                        FilterChip(
                            selected = dayOfWeek == dayNum,
                            onClick = { dayOfWeek = dayNum },
                            label = { Text(name, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Subject dropdown / selection
                Text("Select Subject", style = MaterialTheme.typography.labelMedium)
                if (subjects.isEmpty()) {
                    Text(
                        "No subjects added yet. Please add subjects first.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    var expanded by remember { mutableStateOf(false) }
                    val currentSub = subjects.find { it.id == selectedSubjectId } ?: subjects.first()
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        OutlinedTextField(
                            value = currentSub.name,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            subjects.forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub.name) },
                                    onClick = {
                                        selectedSubjectId = sub.id
                                        teacher = sub.teacher
                                        room = sub.room
                                        isPractical = sub.isPractical
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End (HH:MM)") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Room / Lab") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(checked = isPractical, onCheckedChange = { isPractical = it })
                    Text("Practical / Lab Session", style = MaterialTheme.typography.bodyMedium)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sub = subjects.find { it.id == selectedSubjectId } ?: subjects.firstOrNull()
                    if (sub != null) {
                        onSave(
                            TimetableClass(
                                semester = currentSemester,
                                dayOfWeek = dayOfWeek,
                                subjectId = sub.id,
                                subjectName = sub.name,
                                teacher = teacher.trim(),
                                room = room.trim(),
                                startTime = startTime.trim(),
                                endTime = endTime.trim(),
                                isPractical = isPractical
                            )
                        )
                    }
                },
                enabled = subjects.isNotEmpty()
            ) {
                Text("Add Class")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddAssignmentDialog(
    currentSemester: Int,
    subjects: List<BcaSubject>,
    onDismiss: () -> Unit,
    onSave: (Assignment) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, 3)
        mutableStateOf(java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(cal.time))
    }
    var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: 0L) }
    var priority by remember { mutableStateOf("MEDIUM") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Assignment") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Assignment Title *") },
                    placeholder = { Text("e.g. C Pointers Lab Worksheet") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Subject", style = MaterialTheme.typography.labelMedium)
                if (subjects.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    val currentSub = subjects.find { it.id == selectedSubjectId } ?: subjects.first()
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentSub.name)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            subjects.forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub.name) },
                                    onClick = {
                                        selectedSubjectId = sub.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Priority", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("LOW", "MEDIUM", "HIGH").forEach { p ->
                        FilterChip(
                            selected = priority == p,
                            onClick = { priority = p },
                            label = { Text(p) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description / Tasks") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val subName = subjects.find { it.id == selectedSubjectId }?.name ?: "BCA Subject"
                        onSave(
                            Assignment(
                                semester = currentSemester,
                                subjectId = selectedSubjectId,
                                subjectName = subName,
                                title = title.trim(),
                                description = description.trim(),
                                dueDate = dueDate.trim(),
                                priority = priority
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Assignment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddExamDialog(
    currentSemester: Int,
    subjects: List<BcaSubject>,
    onDismiss: () -> Unit,
    onSave: (ExamEntry) -> Unit
) {
    var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: 0L) }
    var examType by remember { mutableStateOf("Mid-Sem") }
    var date by remember {
        val cal = java.util.Calendar.getInstance()
        cal.add(java.util.Calendar.DAY_OF_YEAR, 14)
        mutableStateOf(java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(cal.time))
    }
    var time by remember { mutableStateOf("10:00 AM") }
    var room by remember { mutableStateOf("Room 204") }

    val examTypes = listOf("Internal", "Mid-Sem", "Practical", "End-Sem", "Viva")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Exam") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Subject", style = MaterialTheme.typography.labelMedium)
                if (subjects.isNotEmpty()) {
                    var expanded by remember { mutableStateOf(false) }
                    val currentSub = subjects.find { it.id == selectedSubjectId } ?: subjects.first()
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(currentSub.name)
                        }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            subjects.forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub.name) },
                                    onClick = {
                                        selectedSubjectId = sub.id
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Text("Exam Type", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    examTypes.take(3).forEach { t ->
                        FilterChip(
                            selected = examType == t,
                            onClick = { examType = t },
                            label = { Text(t, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    examTypes.drop(3).forEach { t ->
                        FilterChip(
                            selected = examType == t,
                            onClick = { examType = t },
                            label = { Text(t, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Exam Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Time") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = room,
                        onValueChange = { room = it },
                        label = { Text("Room / Hall") },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val subName = subjects.find { it.id == selectedSubjectId }?.name ?: "Subject"
                    onSave(
                        ExamEntry(
                            semester = currentSemester,
                            subjectId = selectedSubjectId,
                            subjectName = subName,
                            examType = examType,
                            date = date.trim(),
                            time = time.trim(),
                            room = room.trim()
                        )
                    )
                }
            ) {
                Text("Schedule")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddTopicDialog(
    subjectId: Long,
    semester: Int,
    existingUnits: List<Int>,
    onDismiss: () -> Unit,
    onSave: (SyllabusTopic) -> Unit
) {
    var unitNumber by remember { mutableIntStateOf(existingUnits.maxOrNull() ?: 1) }
    var unitTitle by remember { mutableStateOf("Unit $unitNumber: ") }
    var topicTitle by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Syllabus Topic") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = unitNumber.toString(),
                    onValueChange = { unitNumber = it.toIntOrNull() ?: 1 },
                    label = { Text("Unit Number (1-5)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = unitTitle,
                    onValueChange = { unitTitle = it },
                    label = { Text("Unit Title") },
                    placeholder = { Text("e.g. Unit 1: Data Types & Control Structures") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = topicTitle,
                    onValueChange = { topicTitle = it },
                    label = { Text("Topic Name *") },
                    placeholder = { Text("e.g. Pointers & Dynamic Allocation") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (topicTitle.isNotBlank()) {
                        onSave(
                            SyllabusTopic(
                                subjectId = subjectId,
                                semester = semester,
                                unitNumber = unitNumber,
                                unitTitle = unitTitle.trim(),
                                topicTitle = topicTitle.trim(),
                                status = "NOT_STARTED"
                            )
                        )
                    }
                },
                enabled = topicTitle.isNotBlank()
            ) {
                Text("Add Topic")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun AddNoteDialog(
    currentSemester: Int,
    subjects: List<BcaSubject>,
    onDismiss: () -> Unit,
    onSave: (QuickNote) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Programming") }
    var selectedSubjectId by remember { mutableStateOf<Long?>(subjects.firstOrNull()?.id) }

    val categories = listOf("Programming", "Mathematics", "Computer Fundamentals", "English", "Labs", "Exams", "General")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Quick Note") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Note Title *") },
                    placeholder = { Text("e.g. Recursion vs Iteration rules") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Category", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.take(3).forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.drop(3).take(3).forEach { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Note Details *") },
                    placeholder = { Text("Write formulas, key concepts, quick snippets...") },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        val subName = subjects.find { it.id == selectedSubjectId }?.name ?: ""
                        onSave(
                            QuickNote(
                                semester = currentSemester,
                                subjectId = selectedSubjectId,
                                subjectName = subName,
                                category = selectedCategory,
                                title = title.trim(),
                                content = content.trim()
                            )
                        )
                    }
                },
                enabled = title.isNotBlank() && content.isNotBlank()
            ) {
                Text("Save Note")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
