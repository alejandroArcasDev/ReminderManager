package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val interval: Interval
)