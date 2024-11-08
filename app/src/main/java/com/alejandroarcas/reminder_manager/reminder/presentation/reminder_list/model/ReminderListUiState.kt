package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model

import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder

sealed class ReminderListUiState {
    data object LOADING: ReminderListUiState()
    data object SUCCESS: ReminderListUiState()
    data object EMPTY: ReminderListUiState()
}