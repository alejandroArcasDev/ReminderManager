package com.alejandroarcas.reminder_manager.reminder.use_cases

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository
import java.time.LocalDateTime

class UpsertReminder (
    private val reminderRepository: ReminderRepository
) {
    suspend operator fun invoke(
        id: Int? = 0,
        title: String,
        interval: Interval,
        dateTime: LocalDateTime?,
        active: Boolean
        //time: LocalTime?
    ): Boolean {

        if (title.isEmpty()) {
            return false
        }

        val reminder = Reminder(
            id = id?: 0,
            title = title,
            interval = interval,
            dateTime = if (interval == Interval.ONCE) dateTime else null,
            time = if (interval == Interval.DAILY) dateTime?.toLocalTime() else null,
            active = active
        )


        reminderRepository.upsertReminder(reminder)
        return true

    }
}