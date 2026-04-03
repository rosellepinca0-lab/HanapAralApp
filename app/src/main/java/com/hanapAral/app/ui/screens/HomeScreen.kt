package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isAdmin: Boolean,
    isProfileShowing: Boolean,
    isEditingProfile: Boolean,
    onAdminClick: () -> Unit,
    onProfileClick: () -> Unit,
    onEditClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onCreateGroupClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isProfileShowing) "Profile" else "HanapAral",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.text_primary)
                        )
                        if (!isProfileShowing) {
                            Text(
                                text = "Find your study group",
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.text_secondary)
                            )
                        }
                    }
                },
                actions = {
                    if (isAdmin && !isProfileShowing) {
                        IconButton(onClick = onAdminClick) {
                            Icon(
                                Icons.Default.AdminPanelSettings,
                                contentDescription = "Admin",
                                tint = colorResource(id = R.color.accent_primary)
                            )
                        }
                    }
                    
                    if (isProfileShowing) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                if (isEditingProfile) Icons.Default.Close else Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = colorResource(id = R.color.accent_primary)
                            )
                        }
                        IconButton(onClick = onSignOutClick) {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = "Sign Out",
                                tint = Color.Red
                            )
                        }
                    }

                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = colorResource(id = R.color.accent_primary).copy(alpha = 0.1f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    if (isProfileShowing) Icons.Default.Home else Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = colorResource(id = R.color.accent_primary)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.bg_primary)
                )
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !isProfileShowing,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                FloatingActionButton(
                    onClick = onCreateGroupClick,
                    containerColor = colorResource(id = R.color.accent_primary),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(16.dp),
                    elevation = FloatingActionButtonDefaults.elevation(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Create Group")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("New Group", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.bg_primary))
        ) {
            content()
        }
    }
}
