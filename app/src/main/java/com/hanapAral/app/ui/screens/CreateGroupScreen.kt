package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit,
    isLoading: Boolean = false
) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Study Group", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.bg_primary)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.bg_primary))
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text(
                "Group Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.accent_primary),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            CreateGroupTextField(
                label = "Group Name",
                value = name,
                onValueChange = { name = it },
                placeholder = "e.g. BSIT 3-1 Study Buddies",
                icon = Icons.Default.Groups
            )

            Spacer(modifier = Modifier.height(20.dp))

            CreateGroupTextField(
                label = "Subject",
                value = subject,
                onValueChange = { subject = it },
                placeholder = "e.g. Mobile Application Development",
                icon = Icons.Default.MenuBook
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                placeholder = { Text("What is this group about?") },
                leadingIcon = { Icon(Icons.Default.Description, null) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = colorResource(id = R.color.accent_primary),
                    focusedLabelColor = colorResource(id = R.color.accent_primary)
                )
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { onCreate(name, subject, description) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank() && subject.isNotBlank() && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.accent_primary)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("CREATE GROUP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        leadingIcon = { Icon(icon, null) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.accent_primary),
            focusedLabelColor = colorResource(id = R.color.accent_primary)
        )
    )
}
