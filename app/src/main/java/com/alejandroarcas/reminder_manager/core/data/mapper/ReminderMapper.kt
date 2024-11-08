package com.alejandroarcas.reminder_manager.core.data.mapper

import com.alejandroarcas.reminder_manager.core.data.database.ReminderEntity
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder

fun ReminderEntity.toReminder(): Reminder {
    return Reminder(
        id = this.id,
        title = this.title,
        interval = this.interval,
        dateTime = this.dateTime,
        time = this.time,
        active = this.active
    )
}

fun Reminder.toReminderEntity(): ReminderEntity {
    return ReminderEntity(
        id = this.id,
        title = this.title,
        interval = this.interval,
        dateTime = this.dateTime,
        time = this.time,
        active = this.active
    )
}