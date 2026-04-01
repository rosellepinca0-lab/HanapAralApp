package com.hanapAral.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.hanapAral.app.data.model.StudyGroup

@Composable
fun GroupsScreen(
    groups: List<StudyGroup>,
    currentUserId: String = "",
    isLoading: Boolean = false,
    onGroupClick: (StudyGroup) -> Unit = {},
    onJoinClick: (StudyGroup) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Available Study Groups",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.text_primary),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = colorResource(id = R.color.accent_primary))
            }
        } else if (groups.isEmpty()) {
            EmptyGroupsState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(groups) { group ->
                    val isMember = group.members.contains(currentUserId)
                    GroupCard(
                        group = group,
                        isMember = isMember,
                        onCardClick = { onGroupClick(group) },
                        onJoinClick = { onJoinClick(group) }
                    )
                }
            }
        }
    }
}
@Composable
fun GroupCard(
    group: StudyGroup,
    isMember: Boolean,
    onCardClick: () -> Unit,
    onJoinClick: () -> Unit
) {
    Card(
        onClick = onCardClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorResource(id = R.color.surface)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
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
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Admin: ${group.adminName}",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_secondary),
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${group.members.size}/${group.maxMembers} members",
                    fontSize = 12.sp,
                    color = colorResource(id = R.color.text_secondary),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Button(
                    onClick = onJoinClick,
                    enabled = !isMember,
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMember) colorResource(id = R.color.text_secondary) else colorResource(id = R.color.accent_primary),
                        disabledContainerColor = colorResource(id = R.color.divider)
                    )
                ) {
                    Text(
                        text = if (isMember) "Joined" else "Join",
                        fontSize = 12.sp,
                        color = if (isMember) colorResource(id = R.color.text_primary) else Color.White
                    )
                }
            }
        }
    }
}
