package com.hanapAral.app.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    name: String,
    email: String,
    photoUrl: String?,
    initialCourse: String,
    initialYearLevel: String,
    yearLevels: Array<String>,
    isLoading: Boolean = false,
    isReadOnly: Boolean = true,
    onUpdateClick: (course: String, yearLevel: String) -> Unit = { _, _ -> }
) {
    var course by remember { mutableStateOf(initialCourse) }
    var yearLevel by remember { mutableStateOf(initialYearLevel) }
    var expanded by remember { mutableStateOf(false) }
    var showViewPopup by remember { mutableStateOf(false) }

    LaunchedEffect(initialCourse, initialYearLevel) {
        course = initialCourse
        yearLevel = initialYearLevel
    }

    // Profile View Popup
    if (showViewPopup) {
        ProfileViewPopup(
            name = name,
            email = email,
            photoUrl = photoUrl,
            course = course,
            yearLevel = yearLevel,
            onDismiss = { showViewPopup = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Profile Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(photoUrl)
                .crossfade(true)
                .placeholder(R.drawable.ic_profile_placeholder)
                .error(R.drawable.ic_profile_placeholder)
                .build(),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(140.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray.copy(alpha = 0.2f), CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Name and Email
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Text(
            text = email,
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_secondary)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Course Field
        OutlinedTextField(
            value = course,
            onValueChange = { course = it },
            label = { Text("Course") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = isReadOnly,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isReadOnly) Color.Gray else Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Gray,
                unfocusedLabelColor = Color.Gray,
                cursorColor = if (isReadOnly) Color.Transparent else Color.Black
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Year Level Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded && !isReadOnly,
            onExpandedChange = { if (!isReadOnly) expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = yearLevel,
                onValueChange = {},
                readOnly = true,
                label = { Text("Year Level") },
                trailingIcon = {
                    if (!isReadOnly) {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isReadOnly) Color.Gray else Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Gray,
                    unfocusedLabelColor = Color.Gray
                )
            )
            if (!isReadOnly) {
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    yearLevels.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                yearLevel = selectionOption
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Update Button
        if (!isReadOnly) {
            Button(
                onClick = {
                    onUpdateClick(course, yearLevel)
                    showViewPopup = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isLoading && course.isNotBlank(),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.accent_primary)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Update Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}
