package com.alejandroarcas.reminder_manager.core.data.database

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Converters {
    private val dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    private val timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME

    // Convertir LocalDateTime a String
    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime?): String? {
        return dateTime?.format(dateTimeFormatter)
    }

    // Convertir String a LocalDateTime
    @TypeConverter
    fun toLocalDateTime(dateTimeString: String?): LocalDateTime? {
        return dateTimeString?.let { LocalDateTime.parse(it, dateTimeFormatter) }
    }

    // Convertir LocalTime a String
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.format(timeFormatter)
    }

    // Convertir String a LocalTime
    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it, timeFormatter) }
    }
}