package com.alejandroarcas.reminder_manager.reminder.domain.model

import java.time.LocalDateTime
import java.time.LocalTime

data class Reminder(
    val id: Int = 0,
    val title: String,
    val interval: Interval,
    val dateTime: LocalDateTime?,  // If Interval.ONCE
    val time: LocalTime?, // If Interval.DAILY
    val active: Boolean
)

enum class Interval {
    ONCE, DAILY
}

