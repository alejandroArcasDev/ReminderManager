package com.alejandroarcas.reminder_manager.core.data.repository

import com.alejandroarcas.reminder_manager.core.data.database.ReminderDB
import com.alejandroarcas.reminder_manager.core.data.mapper.toReminder
import com.alejandroarcas.reminder_manager.core.data.mapper.toReminderEntity
import com.alejandroarcas.reminder_manager.reminder.domain.repository.ReminderRepository
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder

class ReminderRepositoryImpl(db: ReminderDB): ReminderRepository {

    private val reminderDao = db.reminderDao

    override suspend fun upsertReminder(reminder: Reminder) {
        return reminderDao.upsertReminderEntity(reminder.toReminderEntity())
    }

    override suspend fun getAllReminders(): List<Reminder> {
        return reminderDao.getAllReminders().map { it.toReminder() }
    }

    override suspend fun getReminderById(id: Int): Reminder {
        return reminderDao.getReminderById(id).toReminder()
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        return reminderDao.deleteReminder(reminder.toReminderEntity())
    }
}