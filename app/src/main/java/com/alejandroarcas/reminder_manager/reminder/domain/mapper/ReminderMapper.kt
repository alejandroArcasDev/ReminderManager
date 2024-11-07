package com.alejandroarcas.reminder_manager.reminder.domain.mapper

import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval

fun Interval.toPresentation() = when (this) {
    Interval.ONCE -> "Once"
    Interval.DAILY -> "Daily"
}
