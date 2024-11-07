package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReminderEntity::class], version = 1)
abstract class ReminderDB(): RoomDatabase() {
    abstract val reminderDao: ReminderDao
}
