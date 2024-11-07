package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model

import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder

sealed class ReminderListUiState {
    data object LOADING: ReminderListUiState()
    data class SUCCESS(val list: List<Reminder>): ReminderListUiState()
    data object EMPTY: ReminderListUiState()
}