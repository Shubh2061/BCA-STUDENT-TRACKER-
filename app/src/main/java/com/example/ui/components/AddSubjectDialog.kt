package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.BcaSubject

@Composable
fun AddSubjectDialog(
    currentSemester: Int,
    subjectToEdit: BcaSubject? = null,
    onDismiss: () -> Unit,
    onSave: (BcaSubject) -> Unit
) {
    var name by remember { mutableStateOf(subjectToEdit?.name ?: "") }
    var code by remember { mutableStateOf(subjectToEdit?.code ?: "") }
    var teacher by remember { mutableStateOf(subjectToEdit?.teacher ?: "") }
    var room by remember { mutableStateOf(subjectToEdit?.room ?: "") }
    var isPractical by remember { mutableStateOf(subjectToEdit?.isPractical ?: false) }
    var isProgramming by remember { mutableStateOf(subjectToEdit?.isProgramming ?: false) }
    var credits by remember { mutableStateOf((subjectToEdit?.credits ?: 4).toString()) }
    var examDate by remember { mutableStateOf(subjectToEdit?.examDate ?: "") }
    var notes by remember { mutableStateOf(subjectToEdit?.notes ?: "") }

    val isEdit = subjectToEdit != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEdit) "Edit Subject" else "Add BCA Subject (Sem $currentSemester)")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Subject Name *") },
                    placeholder = { Text("e.g. Programming in C") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = code,
                        onValueChange = { code = it },
                        label = { Text("Code") },
                        placeholder = { Text("e.g. BCA-101") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = credits,
                        onValueChange = { credits = it },
                        label = { Text("Credits") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = teacher,
                    onValueChange = { teacher = it },
                    label = { Text("Teacher Name") },
                    placeholder = { Text("e.g. Dr. Sharma") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = room,
                        onValueChange = { room = it },
                        label = { Text("Classroom / Lab") },
                        placeholder = { Text("Room 204") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = examDate,
                        onValueChange = { examDate = it },
                        label = { Text("Exam Date") },
                        placeholder = { Text("YYYY-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPractical,
                        onCheckedChange = { isPractical = it }
                    )
                    Text("Practical / Lab Subject", style = MaterialTheme.typography.bodyMedium)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isProgramming,
                        onCheckedChange = { isProgramming = it }
                    )
                    Text("Programming / Coding Subject", style = MaterialTheme.typography.bodyMedium)
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Description") },
                    placeholder = { Text("Key syllabus books, references...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val sub = subjectToEdit?.copy(
                            name = name.trim(),
                            code = code.trim(),
                            teacher = teacher.trim(),
                            room = room.trim(),
                            isPractical = isPractical,
                            isProgramming = isProgramming,
                            credits = credits.toIntOrNull() ?: 4,
                            examDate = examDate.trim(),
                            notes = notes.trim()
                        ) ?: BcaSubject(
                            semester = currentSemester,
                            name = name.trim(),
                            code = code.trim(),
                            teacher = teacher.trim(),
                            room = room.trim(),
                            isPractical = isPractical,
                            isProgramming = isProgramming,
                            credits = credits.toIntOrNull() ?: 4,
                            examDate = examDate.trim(),
                            notes = notes.trim()
                        )
                        onSave(sub)
                    }
                },
                enabled = name.isNotBlank()
            ) {
                Text(if (isEdit) "Save" else "Add Subject")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
