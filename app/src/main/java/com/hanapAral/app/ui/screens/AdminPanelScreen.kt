package com.hanapAral.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    groupCreationEnabled: Boolean,
    joinEnabled: Boolean,
    maxMembers: Long,
    announcementHeader: String,
    notifications: List<String>,
    isLoading: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.surface),
                    titleContentColor = colorResource(id = R.color.text_primary)
                )
            )
        },
        containerColor = colorResource(id = R.color.bg_primary)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 24.dp),
                    color = colorResource(id = R.color.accent_primary)
                )
            }

            Text(
                text = "New User Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_primary),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (notifications.isEmpty()) {
                Text(
                    text = "No new notifications",
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_secondary),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            } else {
                notifications.forEach { notification ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = notification,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = "Remote Config Status",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_primary),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            ConfigCard(text = "Group Creation: ${if (groupCreationEnabled) "Enabled" else "Disabled"}")
            ConfigCard(text = "Join Groups: ${if (joinEnabled) "Enabled" else "Disabled"}")
            ConfigCard(text = "Max Members: $maxMembers")
            ConfigCard(text = "Announcement Header: $announcementHeader")

            Text(
                text = "To toggle features: Go to Firebase Console → Remote Config → Edit values → Publish",
                fontSize = 13.sp,
                color = colorResource(id = R.color.text_secondary),
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun ConfigCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface)
        )
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(16.dp),
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_primary)
        )
    }
}
