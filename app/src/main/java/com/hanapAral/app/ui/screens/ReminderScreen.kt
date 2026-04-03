package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

data class Reminder(
    val id: String,
    val title: String,
    val time: String,
    val isActive: Boolean
)

@Composable
fun ReminderScreen(
    reminders: List<Reminder>,
    onDeleteClick: (Reminder) -> Unit,
    onToggleActive: (Reminder, Boolean) -> Unit
) {
    if (reminders.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Alarm,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = colorResource(id = R.color.text_secondary).copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No study reminders set.",
                    color = colorResource(id = R.color.text_secondary)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(reminders, key = { it.id }) { reminder ->
                ReminderItem(
                    reminder = reminder,
                    onDelete = { onDeleteClick(reminder) },
                    onToggle = { active -> onToggleActive(reminder, active) }
                )
            }
        }
    }
}

@Composable
fun ReminderItem(
    reminder: Reminder,
    onDelete: () -> Unit,
    onToggle: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.isActive) Color.White else colorResource(id = R.color.bg_primary)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (reminder.isActive) 2.dp else 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (reminder.isActive) 
                    colorResource(id = R.color.accent_primary).copy(alpha = 0.1f) 
                    else Color.LightGray.copy(alpha = 0.3f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = null,
                        tint = if (reminder.isActive) colorResource(id = R.color.accent_primary) else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (reminder.isActive) colorResource(id = R.color.text_primary) else Color.Gray
                )
                Text(
                    text = reminder.time,
                    fontSize = 14.sp,
                    color = if (reminder.isActive) colorResource(id = R.color.accent_primary) else Color.Gray
                )
            }

            Switch(
                checked = reminder.isActive,
                onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.accent_primary),
                    checkedTrackColor = colorResource(id = R.color.accent_primary).copy(alpha = 0.5f)
                )
            )

            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f)
                )
            }
        }
    }
}
