package com.hanapAral.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R
import com.hanapAral.app.data.model.StudyGroup

@Composable
fun GroupsScreen(
    groups: List<StudyGroup>,
    currentUserId: String,
    isLoading: Boolean,
    onGroupClick: (StudyGroup) -> Unit,
    onJoinClick: (StudyGroup) -> Unit
) {
    if (isLoading && groups.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = colorResource(id = R.color.accent_primary))
        }
    } else if (groups.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Group,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = colorResource(id = R.color.text_secondary).copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No study groups yet.",
                    color = colorResource(id = R.color.text_secondary),
                    fontSize = 16.sp
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(groups, key = { it.id }) { group ->
                GroupItem(
                    group = group,
                    isMember = group.members.contains(currentUserId),
                    onGroupClick = { onGroupClick(group) },
                    onJoinClick = { onJoinClick(group) }
                )
            }
        }
    }
}

@Composable
fun GroupItem(
    group: StudyGroup,
    isMember: Boolean,
    onGroupClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onGroupClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = group.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.text_primary)
                    )
                    Text(
                        text = group.subject,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.accent_primary),
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = colorResource(id = R.color.bg_primary),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = colorResource(id = R.color.text_secondary)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${group.members.size}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.text_secondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = group.description,
                fontSize = 14.sp,
                color = colorResource(id = R.color.text_secondary),
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (isMember) {
                OutlinedButton(
                    onClick = onGroupClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = colorResource(id = R.color.accent_primary)
                    )
                ) {
                    Text("GO TO GROUP", fontWeight = FontWeight.Bold)
                }
            } else {
                Button(
                    onClick = onJoinClick,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.accent_primary)
                    )
                ) {
                    Text("JOIN GROUP", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
