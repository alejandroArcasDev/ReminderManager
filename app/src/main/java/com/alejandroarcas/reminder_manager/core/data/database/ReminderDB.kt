package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ReminderEntity::class], version = 2)
@TypeConverters(Converters::class)
abstract class ReminderDB: RoomDatabase() {
    abstract val reminderDao: ReminderDao
}
