package com.alejandroarcas.reminder_manager.reminder.use_cases

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository

class UpsertReminder (
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(
        title: String,
        interval: Interval
    ): Boolean {

        if (title.isEmpty()) {
            return false
        }

        val reminder = Reminder(
            title = title,
            interval = interval
        )

        reminderRepository.upsertReminder(reminder)
        return true

    }
}