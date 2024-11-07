package com.alejandroarcas.reminder_manager.reminder.domain.repository

import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder

interface ReminderRepository {

    suspend fun upsertReminder(reminder: Reminder)

    suspend fun getAllReminders(): List<Reminder>

    suspend fun getReminderById(id: Int): Reminder

    suspend fun deleteReminder(reminder: Reminder)
}