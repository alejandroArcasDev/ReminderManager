package com.alejandroarcas.reminder_manager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alejandroarcas.reminder_manager.core.navigation.NavigationWrapper
import com.alejandroarcas.reminder_manager.core.utils.theme.ReminderManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReminderManagerTheme {
                NavigationWrapper()
            }
        }
    }
}