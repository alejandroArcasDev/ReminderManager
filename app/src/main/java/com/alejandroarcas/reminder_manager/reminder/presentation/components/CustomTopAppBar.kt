package com.alejandroarcas.reminder_manager.reminder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(title: String, navigateBack: () -> Unit, action: (() -> Unit)? = null ) {
    TopAppBar(
        title = {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(title)

                if (action != null) { // Check is now necessary
                    IconButton(onClick = { action() }) {
                        // Replace with your desired icon
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Action",
                            tint = Color.Red
                        )
                    }
                }
            }
                },
        navigationIcon = {
            IconButton(
                onClick = { navigateBack() }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}