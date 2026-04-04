package com.hanapAral.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isAdmin: Boolean = false,
    isProfileShowing: Boolean = false,
    isEditingProfile: Boolean = false,
    onAdminClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {},
    onCreateGroupClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HanapAral",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.text_primary)
                        )
                        if (isAdmin) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = colorResource(id = R.color.accent_primary).copy(alpha = 0.1f),
                                shape = CircleShape,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Admin",
                                    tint = colorResource(id = R.color.accent_primary),
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                        }
                    }
                },
                actions = {
                    if (isAdmin) {
                        TextButton(onClick = onAdminClick) {
                            Text(
                                text = "Settings",
                                color = colorResource(id = R.color.accent_primary)
                            )
                        }
                    }

                    if (isProfileShowing) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Profile",
                                tint = if (isEditingProfile) colorResource(id = R.color.accent_primary) else colorResource(id = R.color.text_secondary)
                            )
                        }
                    }
                    
                    TextButton(onClick = onProfileClick) {
                        Text(
                            text = if (isProfileShowing) "Group Chats" else "Profile",
                            color = colorResource(id = R.color.accent_primary),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    TextButton(onClick = onSignOutClick) {
                        Text(
                            text = "Sign Out",
                            color = colorResource(id = R.color.text_secondary)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.surface)
                )
            )
        },
        floatingActionButton = {
            if (!isProfileShowing) {
                ExtendedFloatingActionButton(
                    onClick = onCreateGroupClick,
                    containerColor = colorResource(id = R.color.accent_primary),
                    contentColor = Color.White,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text(text = "Create Group") }
                )
            }
        },
        containerColor = colorResource(id = R.color.bg_primary)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(isAdmin = true) {
        Box(Modifier.fillMaxSize()) {
            Text("Main Content Area", modifier = Modifier.padding(16.dp))
        }
    }
}
