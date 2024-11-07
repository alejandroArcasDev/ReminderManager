package com.alejandroarcas.reminder_manager.reminder.use_cases

import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository

class GetReminder(
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(id: Int) = reminderRepository.getReminderById(id)
}