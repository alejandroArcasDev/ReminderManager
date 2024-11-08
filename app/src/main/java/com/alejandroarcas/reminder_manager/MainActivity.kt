package com.alejandroarcas.reminder_manager

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.alejandroarcas.reminder_manager.core.navigation.NavigationWrapper
import com.alejandroarcas.reminder_manager.core.utils.theme.ReminderManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val notificationPermissions = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){ isGranted ->
            if (!isGranted) {
                Log.e("MainActivity", "Notification permission not granted.")
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissions.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        setContent {
            ReminderManagerTheme {

                NavigationWrapper()

            }
        }


    }


}
