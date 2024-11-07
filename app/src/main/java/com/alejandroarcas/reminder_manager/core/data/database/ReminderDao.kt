package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ReminderDao {
    @Upsert
    suspend fun upsertReminderEntity(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM reminderentity")
    suspend fun getAllReminders(): List<ReminderEntity>

    @Delete
    suspend fun deleteReminder(reminderEntity: ReminderEntity)
}