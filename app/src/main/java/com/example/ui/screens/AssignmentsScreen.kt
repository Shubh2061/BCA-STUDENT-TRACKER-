package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.Assignment
import com.example.ui.components.AddAssignmentDialog
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun AssignmentsScreen(
    viewModel: BcaViewModel,
    onBack: () -> Unit
) {
    val currentSemester by viewModel.selectedSemester.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val subjects by viewModel.subjects.collectAsState()

    var filter by remember { mutableStateOf("ALL") } // ALL, PENDING, COMPLETED, HIGH_PRIORITY
    var showAddDialog by remember { mutableStateOf(false) }

    val filtered = remember(assignments, filter) {
        when (filter) {
            "PENDING" -> assignments.filter { !it.isCompleted }
            "COMPLETED" -> assignments.filter { it.isCompleted }
            "HIGH_PRIORITY" -> assignments.filter { it.priority == "HIGH" && !it.isCompleted }
            else -> assignments
        }
    }

    val pendingCount = assignments.count { !it.isCompleted }

    Scaffold(
        topBar = {
            Surface(tonalElevation = 2.dp, modifier = Modifier.statusBarsPadding()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            IconButton(onClick = onBack) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                            Column {
                                Text(
                                    text = "📝 Assignment Tracker",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Sem $currentSemester • $pendingCount Pending",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Button(
                            onClick = { showAddDialog = true },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("New")
                        }
                    }

                    // Filter chips
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(listOf("ALL", "PENDING", "HIGH_PRIORITY", "COMPLETED")) { f ->
                            FilterChip(
                                selected = filter == f,
                                onClick = { filter = f },
                                label = {
                                    Text(
                                        when (f) {
                                            "ALL" -> "All (${assignments.size})"
                                            "PENDING" -> "Pending ($pendingCount)"
                                            "HIGH_PRIORITY" -> "High Priority"
                                            "COMPLETED" -> "Completed"
                                            else -> f
                                        },
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.TaskAlt, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(48.dp))
                    Text("No assignments found in this list", fontWeight = FontWeight.Bold)
                    OutlinedButton(onClick = { showAddDialog = true }) {
                        Text("+ Add Assignment")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filtered, key = { it.id }) { assignment ->
                    AssignmentCard(
                        assignment = assignment,
                        onToggle = { viewModel.toggleAssignment(assignment) },
                        onDelete = { viewModel.deleteAssignment(assignment) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

    if (showAddDialog) {
        AddAssignmentDialog(
            currentSemester = currentSemester,
            subjects = subjects,
            onDismiss = { showAddDialog = false },
            onSave = {
                viewModel.addAssignment(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AssignmentCard(
    assignment: Assignment,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Checkbox(
                    checked = assignment.isCompleted,
                    onCheckedChange = { onToggle() }
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (assignment.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${assignment.subjectName} • Due: ${assignment.dueDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    color = when (assignment.priority) {
                        "HIGH" -> DangerRed.copy(alpha = 0.15f)
                        "LOW" -> SuccessGreen.copy(alpha = 0.15f)
                        else -> WarningAmber.copy(alpha = 0.15f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = assignment.priority,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = when (assignment.priority) {
                            "HIGH" -> DangerRed
                            "LOW" -> SuccessGreen
                            else -> WarningAmber
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            if (assignment.description.isNotBlank()) {
                Text(
                    text = assignment.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
