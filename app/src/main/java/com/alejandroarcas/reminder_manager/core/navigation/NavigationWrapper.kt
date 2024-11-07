package com.alejandroarcas.reminder_manager.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_add.ReminderAddScreen
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail.ReminderDetailScreen
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.ListViewModel
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.ReminderListScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListScreen) {
        composable<ListScreen> {
            val listViewModel: ListViewModel = hiltViewModel()

            ReminderListScreen(listViewModel) { reminderId ->
                navController.navigate(DetailScreen(reminderId))
            }
        }
        composable<DetailScreen> { backStackEntry ->
            val reminderId = backStackEntry.toRoute<DetailScreen>().id
            ReminderDetailScreen(reminderId)
        }
        composable<AddScreen> {
            ReminderAddScreen()
        }
    }
}