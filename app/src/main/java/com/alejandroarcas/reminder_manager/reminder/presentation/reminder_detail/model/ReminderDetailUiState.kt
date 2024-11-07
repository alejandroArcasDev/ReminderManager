package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail.model

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval

data class ReminderDetailUiState(
    val title: String = "",
    val interval: Interval = Interval.ONCE,
    val isIntervalEditing: Boolean = false,
    val isTitleEditing: Boolean = false,
    val isDropdownExpanded: Boolean = false,
    val isLoading: Boolean = true,
    val isDeleted: Boolean = false
)