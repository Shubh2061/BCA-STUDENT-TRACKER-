package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.model.LabExperiment
import com.example.ui.theme.BrandPrimary
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BcaViewModel

@Composable
fun LabTrackerScreen(
    viewModel: BcaViewModel,
    onBack: () -> Unit
) {
    val experiments by viewModel.labExperiments.collectAsState()
    val subjects by viewModel.subjects.collectAsState()
    val labSubjects = remember(subjects) { subjects.filter { it.isPractical } }

    var showAddDialog by remember { mutableStateOf(false) }

    val completedCount = experiments.count { it.isCompleted }
    val signedFilesCount = experiments.count { it.fileStatus.contains("Signed", ignoreCase = true) }
    val vivaPreparedCount = experiments.count { it.vivaStatus.contains("Prepared", ignoreCase = true) }

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
                                text = "🧪 BCA Practical / Lab Tracker",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Experiments, Lab Files & Viva Readiness",
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
                        Text("Lab Exp")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Summary Cards Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Experiments",
                        count = "$completedCount/${experiments.size}",
                        subtitle = "Done",
                        color = SuccessGreen
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Lab Files",
                        count = "$signedFilesCount/${experiments.size}",
                        subtitle = "Signed",
                        color = MaterialTheme.colorScheme.primary
                    )
                    SummaryMiniCard(
                        modifier = Modifier.weight(1f),
                        title = "Viva Prep",
                        count = "$vivaPreparedCount/${experiments.size}",
                        subtitle = "Ready",
                        color = WarningAmber
                    )
                }
            }

            if (experiments.isEmpty()) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("No Lab Experiments Added", fontWeight = FontWeight.Bold)
                            Text("Track your practical coding assignments, lab file signatures & viva prep.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Button(onClick = { showAddDialog = true }) {
                                Text("+ Add Experiment")
                            }
                        }
                    }
                }
            } else {
                items(experiments, key = { it.id }) { experiment ->
                    LabExperimentCard(
                        experiment = experiment,
                        onToggleCompleted = { viewModel.toggleLabExperiment(experiment) },
                        onUpdateFileStatus = { status ->
                            viewModel.updateLabExperiment(experiment.copy(fileStatus = status))
                        },
                        onUpdateVivaStatus = { status ->
                            viewModel.updateLabExperiment(experiment.copy(vivaStatus = status))
                        },
                        onDelete = { viewModel.deleteLabExperiment(experiment) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }

    if (showAddDialog) {
        AddLabExperimentDialog(
            labSubjects = if (labSubjects.isEmpty()) subjects else labSubjects,
            onDismiss = { showAddDialog = false },
            onSave = {
                viewModel.addLabExperiment(it)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun SummaryMiniCard(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    subtitle: String,
    color: Color
) {
    Surface(
        modifier = modifier,
        color = color.copy(alpha = 0.12f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(count, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = color)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}

@Composable
private fun LabExperimentCard(
    experiment: LabExperiment,
    onToggleCompleted: () -> Unit,
    onUpdateFileStatus: (String) -> Unit,
    onUpdateVivaStatus: (String) -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                    checked = experiment.isCompleted,
                    onCheckedChange = { onToggleCompleted() }
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Exp ${experiment.experimentNumber}: ${experiment.title}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Due: ${experiment.date}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
                }
            }

            // Status Pills Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // File Status Pill
                AssistChip(
                    onClick = {
                        val next = when (experiment.fileStatus) {
                            "Pending" -> "In Progress"
                            "In Progress" -> "Signed & Checked"
                            else -> "Pending"
                        }
                        onUpdateFileStatus(next)
                    },
                    label = { Text("File: ${experiment.fileStatus}") },
                    leadingIcon = {
                        Icon(
                            if (experiment.fileStatus.contains("Signed")) Icons.Default.CheckCircle else Icons.Default.Edit,
                            contentDescription = null,
                            tint = if (experiment.fileStatus.contains("Signed")) SuccessGreen else WarningAmber,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )

                // Viva Status Pill
                AssistChip(
                    onClick = {
                        val next = when (experiment.vivaStatus) {
                            "Not Prepared" -> "Needs Revision"
                            "Needs Revision" -> "Prepared"
                            else -> "Not Prepared"
                        }
                        onUpdateVivaStatus(next)
                    },
                    label = { Text("Viva: ${experiment.vivaStatus}") },
                    leadingIcon = {
                        Icon(
                            if (experiment.vivaStatus == "Prepared") Icons.Default.CheckCircle else Icons.Default.HelpOutline,
                            contentDescription = null,
                            tint = if (experiment.vivaStatus == "Prepared") SuccessGreen else WarningAmber,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }

            if (expanded) {
                if (experiment.codeOrObservations.isNotBlank()) {
                    Text(
                        text = "Observations / Notes: ${experiment.codeOrObservations}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
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
fun AddLabExperimentDialog(
    labSubjects: List<com.example.data.model.BcaSubject>,
    onDismiss: () -> Unit,
    onSave: (LabExperiment) -> Unit
) {
    var expNum by remember { mutableIntStateOf(1) }
    var title by remember { mutableStateOf("") }
    var fileStatus by remember { mutableStateOf("Pending") }
    var vivaStatus by remember { mutableStateOf("Not Prepared") }
    var notes by remember { mutableStateOf("") }
    val labSubId = labSubjects.firstOrNull()?.id ?: 1L

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Lab Experiment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = expNum.toString(),
                    onValueChange = { expNum = it.toIntOrNull() ?: 1 },
                    label = { Text("Experiment Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Experiment Title *") },
                    placeholder = { Text("e.g. Implement Binary Search in C") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes / Formulae / Observations") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(
                            LabExperiment(
                                subjectId = labSubId,
                                experimentNumber = expNum,
                                title = title.trim(),
                                fileStatus = fileStatus,
                                vivaStatus = vivaStatus,
                                codeOrObservations = notes.trim()
                            )
                        )
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("Add Experiment")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
