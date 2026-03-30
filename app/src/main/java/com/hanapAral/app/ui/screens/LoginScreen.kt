package com.hanapAral.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R
@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    onGoogleSignInClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.bg_primary))
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "HanapAral",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.text_primary)
        )

        Text(
            text = "Your Study Group Finder",
            fontSize = 16.sp,
            color = colorResource(id = R.color.text_secondary),
            modifier = Modifier.padding(top = 8.dp)
        )