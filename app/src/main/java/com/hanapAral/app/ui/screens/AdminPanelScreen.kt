package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun AdminPanelScreen(
    maxMembers: Int,
    isJoinEnabled: Boolean,
    isGroupCreationEnabled: Boolean,
    onMaxMembersChange: (Int) -> Unit,
    onJoinToggle: (Boolean) -> Unit,
    onGroupCreationToggle: (Boolean) -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(id = R.color.bg_primary)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorResource(id = R.color.bg_primary)),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                AdminSectionTitle("Global Settings")
            }

            item {
                AdminControlCard(
                    title = "Allow Joining Groups",
                    description = "Users can join existing study groups",
                    icon = Icons.Default.GroupAdd,
                    checked = isJoinEnabled,
                    onCheckedChange = onJoinToggle
                )
            }

            item {
                AdminControlCard(
                    title = "Allow Group Creation",
                    description = "Users can create new study groups",
                    icon = Icons.Default.AddCircle,
                    checked = isGroupCreationEnabled,
                    onCheckedChange = onGroupCreationToggle
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Groups,
                                contentDescription = null,
                                tint = colorResource(id = R.color.accent_primary)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                "Max Members per Group",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Slider(
                            value = maxMembers.toFloat(),
                            onValueChange = { onMaxMembersChange(it.toInt()) },
                            valueRange = 2f..100f,
                            steps = 98,
                            colors = SliderDefaults.colors(
                                thumbColor = colorResource(id = R.color.accent_primary),
                                activeTrackColor = colorResource(id = R.color.accent_primary)
                            )
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("2", color = Color.Gray)
                            Text(
                                text = "$maxMembers",
                                fontWeight = FontWeight.ExtraBold,
                                color = colorResource(id = R.color.accent_primary),
                                fontSize = 20.sp
                            )
                            Text("100", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = R.color.text_secondary),
        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
    )
}

@Composable
fun AdminControlCard(
    title: String,
    description: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = colorResource(id = R.color.accent_primary).copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = colorResource(id = R.color.accent_primary)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.text_primary)
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_secondary)
                )
            }
            
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorResource(id = R.color.accent_primary),
                    checkedTrackColor = colorResource(id = R.color.accent_primary).copy(alpha = 0.5f)
                )
            )
        }
    }
}
