package com.hanapAral.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R
import com.hanapAral.app.data.model.Announcement
import com.hanapAral.app.data.model.ChatMessage
import com.hanapAral.app.data.model.StudyGroup
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    group: StudyGroup?,
    announcements: List<Announcement>,
    chatMessages: List<ChatMessage>,
    currentUserId: String,
    isAdminOfGroup: Boolean = false,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {},
    onPostAnnouncement: (String, String) -> Unit = { _, _ -> },
    onSendMessage: (String) -> Unit = {}
) {
    var chatText by remember { mutableStateOf("") }
    var showAnnouncementDialog by remember { mutableStateOf(false) }
    var announcementText by remember { mutableStateOf("") }

    val announcementTypes = listOf("Group Announcement", "Study Reminder")
    var selectedType by remember { mutableStateOf(announcementTypes[0]) }

    if (showAnnouncementDialog) {
        AlertDialog(
            onDismissRequest = { showAnnouncementDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        if (announcementText.isNotBlank()) {
                            onPostAnnouncement(announcementText, selectedType)
                            announcementText = ""
                            showAnnouncementDialog = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.accent_primary)
                    )
                ) {
                    Text("POST", fontWeight = FontWeight.Bold)
                }
            },
            title = {
                Text(
                    "New Announcement",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select Type:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Radio Button Group
                    Column(Modifier.selectableGroup()) {
                        announcementTypes.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .selectable(
                                        selected = (text == selectedType),
                                        onClick = { selectedType = text },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedType),
                                    onClick = null, // null recommended for accessibility with selectable modifier
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = colorResource(id = R.color.accent_primary)
                                    )
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = announcementText,
                        onValueChange = { announcementText = it },
                        placeholder = {
                            Text(
                                if (selectedType == "Study Reminder") "Enter study details (time, topic)..."
                                else "Enter your announcement...",
                                color = colorResource(id = R.color.text_secondary).copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = colorResource(id = R.color.accent_secondary),
                            unfocusedIndicatorColor = colorResource(id = R.color.divider)
                        )
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(8.dp)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        group?.name ?: "Group Detail",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.text_primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(id = R.color.text_primary)
                        )
                    }
                },
                actions = {
                    if (isAdminOfGroup) {
                        IconButton(onClick = { showAnnouncementDialog = true }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add Announcement",
                                tint = colorResource(id = R.color.accent_primary)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.surface)
                )
            )
        },
        bottomBar = {
            ChatInputBar(
                value = chatText,
                onValueChange = { chatText = it },
                onSendClick = {
                    if (chatText.isNotBlank()) {
                        onSendMessage(chatText)
                        chatText = ""
                    }
                }
            )
        },
        containerColor = colorResource(id = R.color.bg_primary)
    ) { paddingValues ->
        if (isLoading || group == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(id = R.color.accent_primary))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                item {
                    GroupInfoCard(group)
                    Spacer(modifier = Modifier.height(16.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Announcements",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.text_primary)
                        )
                        if (isAdminOfGroup) {
                            TextButton(onClick = { showAnnouncementDialog = true }) {
                                Text("Add New", color = colorResource(id = R.color.accent_primary))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (announcements.isEmpty()) {
                    item {
                        Text(
                            text = "No announcements yet",
                            color = colorResource(id = R.color.text_secondary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    items(announcements) { announcement ->
                        AnnouncementItem(announcement)
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = colorResource(id = R.color.divider))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Group Chat",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.text_primary),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(chatMessages) { message ->
                    ChatMessageItem(message, isCurrentUser = message.senderId == currentUserId)
                }
            }
        }
    }
}

@Composable
fun GroupInfoCard(group: StudyGroup) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.surface)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = group.subject,
                fontSize = 16.sp,
                color = colorResource(id = R.color.accent_primary),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = group.description,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Admin: ${group.adminName}",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary)
            )
            Text(
                text = "${group.members.size}/${group.maxMembers} members",
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary)
            )
        }
    }
}

@Composable
fun AnnouncementItem(announcement: Announcement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.surface))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = announcement.type ?: "Group Announcement",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.accent_primary)
                )
                Text(
                    text = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(Date(announcement.timestamp)),
                    fontSize = 11.sp,
                    color = colorResource(id = R.color.text_secondary)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = announcement.content,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_primary)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "By ${announcement.authorName}",
                fontSize = 11.sp,
                color = colorResource(id = R.color.text_secondary)
            )
        }
    }
}

@Composable
fun ChatMessageItem(message: ChatMessage, isCurrentUser: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        if (!isCurrentUser) {
            Text(
                text = message.senderName,
                fontSize = 11.sp,
                color = colorResource(id = R.color.text_secondary),
                modifier = Modifier.padding(start = 4.dp, bottom = 2.dp)
            )
        }
        Surface(
            color = if (isCurrentUser) colorResource(id = R.color.accent_primary) else colorResource(id = R.color.surface),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 0.dp,
                bottomEnd = if (isCurrentUser) 0.dp else 16.dp
            ),
            tonalElevation = 1.dp
        ) {
            Text(
                text = message.message,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                fontSize = 14.sp,
                color = if (isCurrentUser) Color.White else colorResource(id = R.color.text_primary)
            )
        }
    }
}

@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Surface(
        tonalElevation = 2.dp,
        color = colorResource(id = R.color.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                maxLines = 4,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.accent_primary)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSendClick,
                modifier = Modifier
                    .size(48.dp)
                    .background(colorResource(id = R.color.accent_primary), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}
