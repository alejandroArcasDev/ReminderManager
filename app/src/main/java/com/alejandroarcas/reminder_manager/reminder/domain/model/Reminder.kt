package com.alejandroarcas.reminder_manager.reminder.domain.model

data class Reminder(
    val id: Int,
    val title: String,
    val interval: Interval,
)

enum class Interval {
    ONCE, DAILY
}
