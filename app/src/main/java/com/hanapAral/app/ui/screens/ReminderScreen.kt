package com.hanapAral.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R
import java.util.*

data class StudyReminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val time: String,
    val days: String,
    val isActive: Boolean = true
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    reminders: List<StudyReminder> = emptyList(),
    onAddReminder: (String, String, String) -> Unit = { _, _, _ -> },
    onDeleteReminder: (String) -> Unit = {},
    onToggleReminder: (String, Boolean) -> Unit = { _, _ -> }
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var days by remember { mutableStateOf("") }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Set Study Reminder", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        placeholder = { Text("What to study?") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = time,
                        onValueChange = { time = it },
                        placeholder = { Text("Time (e.g., 8:00 PM)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = days,
                        onValueChange = { days = it },
                        placeholder = { Text("Days (e.g., Mon, Wed, Fri)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.accent_primary)
                        )
                    )
                }
            },