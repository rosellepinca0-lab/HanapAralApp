package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
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
    isLoading: Boolean,
    isReadOnly: Boolean,
    onUpdateClick: (String, String) -> Unit
) {
    var course by remember { mutableStateOf(initialCourse) }
    var yearLevel by remember { mutableStateOf(initialYearLevel) }
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(initialCourse, initialYearLevel) {
        course = initialCourse
        yearLevel = initialYearLevel
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Header
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = CircleShape,
                color = colorResource(id = R.color.accent_primary).copy(alpha = 0.1f)
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = android.R.drawable.ic_menu_report_image) // Fallback
                )
            }
            
            if (!isReadOnly) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = colorResource(id = R.color.accent_primary),
                    tonalElevation = 4.dp
                ) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Change Picture",
                        modifier = Modifier.padding(6.dp),
                        tint = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.text_primary)
        )
        Text(
            text = email,
            fontSize = 14.sp,
            color = colorResource(id = R.color.text_secondary)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Profile Details Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "Academic Information",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.accent_primary)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ProfileTextField(
                    label = "Course",
                    value = course,
                    onValueChange = { course = it },
                    icon = Icons.Default.School,
                    isReadOnly = isReadOnly
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Year Level Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded && !isReadOnly,
                    onExpandedChange = { if (!isReadOnly) expanded = it }
                ) {
                    OutlinedTextField(
                        value = yearLevel,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Year Level") },
                        leadingIcon = { Icon(Icons.Default.CalendarToday, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = colorResource(id = R.color.accent_primary),
                            focusedLabelColor = colorResource(id = R.color.accent_primary)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded && !isReadOnly,
                        onDismissRequest = { expanded = false }
                    ) {
                        yearLevels.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    yearLevel = level
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        if (!isReadOnly) {
            Button(
                onClick = { onUpdateClick(course, yearLevel) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.accent_primary)
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("SAVE CHANGES", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    isReadOnly: Boolean
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = isReadOnly,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = colorResource(id = R.color.accent_primary),
            focusedLabelColor = colorResource(id = R.color.accent_primary)
        )
    )
}

// Dummy helper as painterResource is needed for AsyncImage error
@Composable
fun painterResource(id: Int) = androidx.compose.ui.res.painterResource(id)
