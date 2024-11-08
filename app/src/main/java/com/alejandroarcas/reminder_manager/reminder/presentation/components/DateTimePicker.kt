package com.alejandroarcas.reminder_manager.reminder.presentation.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DateTimePicker(
    interval: Interval,
    dateTime: LocalDateTime? = null,
    time: LocalTime? = null,
    onDateTimeSelected: (LocalDateTime) -> Unit
) {

    println("TIME EN DATE PICKER: $time")

    var selectedDateTime by remember {
        mutableStateOf(dateTime ?: LocalDateTime.now())
    }
    var selectedTime by remember {
        mutableStateOf(time ?: LocalTime.now())
    }
    val context = LocalContext.current

    // Controlar la visibilidad de los diálogos
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Mostrar el texto con la fecha y hora seleccionada
    TextButton(onClick = { if (interval == Interval.ONCE) {
        showDatePicker = true
    } else {
        showTimePicker = true
    } }) {
        Icon(Icons.Rounded.DateRange, contentDescription = "Date")
        Spacer(Modifier.width(5.dp))
        val formattedDateTime = if (interval == Interval.ONCE) {
            selectedDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        } else {
            time!!.format(DateTimeFormatter.ofPattern("HH:mm"))
        }

        Text(text = formattedDateTime)
    }

    // DatePickerDialog when interval is ONCE
    if (showDatePicker && interval == Interval.ONCE) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedDateTime = selectedDateTime.withYear(year).withMonth(month + 1).withDayOfMonth(dayOfMonth)
                showDatePicker = false
                showTimePicker = true // Abre el TimePicker después de seleccionar la fecha
            },
            selectedDateTime.year,
            selectedDateTime.monthValue - 1,
            selectedDateTime.dayOfMonth
        ).show()
    }

    // TimePickerDialog when interval is DAILY
    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                selectedDateTime = selectedDateTime.withHour(hourOfDay).withMinute(minute)
                selectedTime = LocalTime.of(hourOfDay, minute)
                showTimePicker = false

                // Si es DAILY, solo enviamos la hora seleccionada como parte del LocalDateTime de hoy
                onDateTimeSelected(
                    if (interval == Interval.ONCE) selectedDateTime
                    else LocalDateTime.of(LocalDate.now(), selectedTime)
                )
            },
            selectedTime.hour,
            selectedTime.minute,
            true
        ).show()
    }
}