package com.alejandroarcas.reminder_manager.reminder.use_cases

import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository

class DeleteReminder(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(reminder: Reminder) = reminderRepository.deleteReminder(reminder)
}
