package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val interval: Interval,
    val dateTime: LocalDateTime?,  // If Interval.ONCE
    val time: LocalTime? // If Interval.DAILY
)