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