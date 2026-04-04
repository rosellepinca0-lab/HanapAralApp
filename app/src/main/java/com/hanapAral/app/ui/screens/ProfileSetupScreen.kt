package com.hanapAral.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hanapAral.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSetupScreen(
    initialName: String,
    initialEmail: String,
    yearLevels: Array<String>,
    isLoading: Boolean = false,
    onSaveClick: (name: String, email: String, course: String, yearLevel: String, isAdmin: Boolean) -> Unit = { _, _, _, _, _ -> }
) {
    var name by remember { mutableStateOf(initialName) }
    var email by remember { mutableStateOf(initialEmail) }
    var course by remember { mutableStateOf("") }
    var yearLevel by remember { mutableStateOf(if (yearLevels.isNotEmpty()) yearLevels[0] else "") }
    var isAdmin by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    val isAdminEmail = email == "roellepinca0@gmail.com"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {
        Text(
            text = "Set Up Profile",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.text_primary)
        )

        Text(
            text = "Tell us about yourself",
            fontSize = 14.sp,
            color = colorResource(id = R.color.text_secondary),
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = colorResource(id = R.color.divider),
                disabledLabelColor = colorResource(id = R.color.text_secondary)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false,
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = colorResource(id = R.color.divider),
                disabledLabelColor = colorResource(id = R.color.text_secondary)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = course,
            onValueChange = { course = it },
            label = { Text("Course / Program (e.g. BSIT)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.accent_primary),
                focusedLabelColor = colorResource(id = R.color.accent_primary)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Year Level",
            fontSize = 14.sp,
            color = colorResource(id = R.color.text_secondary),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = yearLevel,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(id = R.color.accent_primary)
                )
            )
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

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = if (isAdminEmail) true else isAdmin,
                onCheckedChange = { if (!isAdminEmail) isAdmin = it },
                enabled = !isAdminEmail,
                colors = CheckboxDefaults.colors(
                    checkedColor = colorResource(id = R.color.accent_primary)
                )
            )
            Column {
                Text(
                    text = "Register as Admin",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = colorResource(id = R.color.text_primary)
                )
                if (isAdminEmail) {
                    Text(
                        text = "This account is automatically set as Admin.",
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.accent_primary)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onSaveClick(name, email, course, yearLevel, if (isAdminEmail) true else isAdmin) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading && course.isNotBlank(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.accent_primary)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(text = "Save Profile", fontSize = 16.sp)
            }
        }
    }
}
