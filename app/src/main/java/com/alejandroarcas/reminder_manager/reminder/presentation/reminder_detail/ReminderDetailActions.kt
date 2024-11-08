package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import java.time.LocalDateTime

interface ReminderDetailActions {
    data class UpdateTitle(val title: String) : ReminderDetailActions
    data class UpdateInterval(val interval: Interval) : ReminderDetailActions
    data class DeleteReminder(val reminder: Reminder) : ReminderDetailActions
    data class GetReminder(val id: Int) : ReminderDetailActions
    data class UpdateDateTime(val dateTime: LocalDateTime) : ReminderDetailActions
    data object UpdateReminder : ReminderDetailActions
}