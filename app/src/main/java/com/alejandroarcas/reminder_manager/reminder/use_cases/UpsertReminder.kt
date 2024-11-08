package com.alejandroarcas.reminder_manager.reminder.use_cases

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository
import java.time.LocalDateTime

class UpsertReminder (
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(
        reminder: Reminder
    ): Boolean {

        if (reminder.title.isEmpty()) {
            return false
        }

        val newReminder = Reminder(
            id = reminder.id?: 0,
            title = reminder.title,
            interval = reminder.interval,
            dateTime = if (reminder.interval == Interval.ONCE) reminder.dateTime else null,
            time = if (reminder.interval == Interval.DAILY) reminder.dateTime?.toLocalTime() else null,
            active = reminder.active
        )


        reminderRepository.upsertReminder(newReminder)
        return true

    }
}