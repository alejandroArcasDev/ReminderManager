package com.alejandroarcas.reminder_manager.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_add.ReminderAddScreen
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail.ReminderDetailScreen
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.ReminderListScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen> {
            ReminderListScreen()
        }
        composable<DetailScreen> {
            ReminderDetailScreen()
        }
        composable<AddScreen> {
            ReminderAddScreen()
        }
    }
}