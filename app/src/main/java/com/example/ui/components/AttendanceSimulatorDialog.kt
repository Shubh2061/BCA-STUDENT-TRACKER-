package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.AttendanceCalculator
import com.example.data.model.BcaSubject
import com.example.ui.theme.DangerRed
import com.example.ui.theme.SuccessGreen

@Composable
fun AttendanceSimulatorDialog(
    subject: BcaSubject,
    requiredPct: Double,
    onDismiss: () -> Unit,
    onUpdateCounts: (present: Int, absent: Int) -> Unit
) {
    var presentCount by remember { mutableIntStateOf(subject.presentClasses) }
    var absentCount by remember { mutableIntStateOf(subject.absentClasses) }
    var simulateAttend by remember { mutableIntStateOf(0) }
    var simulateMiss by remember { mutableIntStateOf(0) }

    val currentAnalysis = remember(presentCount, absentCount, requiredPct) {
        AttendanceCalculator.calculate(presentCount, absentCount, requiredPct)
    }

    val simulatedPct = remember(presentCount, absentCount, simulateAttend, simulateMiss) {
        AttendanceCalculator.simulate(presentCount, absentCount, simulateAttend, simulateMiss)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = subject.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Smart Attendance Calculator & Simulator",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Current Stats Card
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (currentAnalysis.isSafe)
                            SuccessGreen.copy(alpha = 0.12f)
                        else
                            DangerRed.copy(alpha = 0.12f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Current Attendance",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "%.1f%%".format(currentAnalysis.currentPercentage),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (currentAnalysis.isSafe) SuccessGreen else DangerRed
                            )
                        }

                        LinearProgressIndicator(
                            progress = { (currentAnalysis.currentPercentage / 100.0).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (currentAnalysis.isSafe) SuccessGreen else DangerRed,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Text(
                            text = currentAnalysis.statusMessage,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (currentAnalysis.classesNeededFor80 > 0) {
                            Text(
                                text = "• Attend ${currentAnalysis.classesNeededFor80} more classes to hit 80%",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Adjust Current Counts
                Text(
                    text = "Adjust Actual Record",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Present: $presentCount", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { if (presentCount > 0) presentCount-- },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("-1") }
                            FilledTonalButton(
                                onClick = { presentCount++ },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("+1") }
                        }
                    }

                    Column {
                        Text("Absent: $absentCount", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilledTonalButton(
                                onClick = { if (absentCount > 0) absentCount-- },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("-1") }
                            FilledTonalButton(
                                onClick = { absentCount++ },
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                            ) { Text("+1") }
                        }
                    }
                }

                HorizontalDivider()

                // Simulation: "What-If" Planner
                Text(
                    text = "🔮 What-If Simulator",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "If I attend next:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { if (simulateAttend > 0) simulateAttend-- },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) { Text("-") }
                        Text("$simulateAttend classes", fontWeight = FontWeight.Bold)
                        FilledTonalButton(
                            onClick = { simulateAttend++ },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) { Text("+") }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "If I miss next:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = { if (simulateMiss > 0) simulateMiss-- },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) { Text("-") }
                        Text("$simulateMiss classes", fontWeight = FontWeight.Bold)
                        FilledTonalButton(
                            onClick = { simulateMiss++ },
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) { Text("+") }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Simulated Attendance:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "%.1f%%".format(simulatedPct),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (simulatedPct >= requiredPct) SuccessGreen else DangerRed
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onUpdateCounts(presentCount, absentCount)
                    onDismiss()
                }
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
