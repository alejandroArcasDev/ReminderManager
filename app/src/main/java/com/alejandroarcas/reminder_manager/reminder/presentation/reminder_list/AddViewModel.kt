package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.use_cases.UpsertReminder
import com.alejandroarcas.reminder_manager.reminder.worker.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addReminderUseCase: UpsertReminder,
) : ViewModel() {

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private val _reminderAddedChannel = Channel<Boolean>()
    val reminderAddedChannel = _reminderAddedChannel.receiveAsFlow()


    private val _dateTime = MutableStateFlow(LocalDateTime.now())
    val dateTime = _dateTime.asStateFlow()

    fun showBottomSheet(value: Boolean) {
        _showBottomSheet.value = value
    }

    fun addReminder(context: Context, title: String, interval: Interval, dateTime: LocalDateTime) {
        viewModelScope.launch {
            val added = addReminderUseCase(
                Reminder(
                    id = 0,
                    title = title,
                    interval = interval,
                    dateTime = dateTime,
                    time = null,
                    active = true
                )
            )
            _reminderAddedChannel.send(added)
            if (interval == Interval.ONCE) {
                scheduleOneTimeReminder(context, title, dateTime)
            } else {
                scheduleDailyReminder(
                    context,
                    title,
                    dateTime.toLocalTime()
                )
            }
        }
    }

    private val _title = MutableStateFlow("")
    val title = _title.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    private val _onceChip = MutableStateFlow(true)
    val onceChip = _onceChip.asStateFlow()

    private val _dailyChip = MutableStateFlow(false)
    val dailyChip = _dailyChip.asStateFlow()

    fun updateChipOnClick(label: String) {
        _onceChip.value = false
        _dailyChip.value = false

        when (label) {
            "once" -> _onceChip.value = true
            "daily" -> _dailyChip.value = true
        }
    }

    fun chipValueToUseCase(): Interval {
        return if (_onceChip.value) Interval.ONCE
        else Interval.DAILY
    }

    fun resetBottomSheetValues() {
        _title.value = ""
        _onceChip.value = true
        _dailyChip.value = false
        _dateTime.value = LocalDateTime.now()
    }

    fun setDate(dateTime: LocalDateTime) {
        _dateTime.value = dateTime
    }

    fun scheduleOneTimeReminder(context: Context, title: String, dateTime: LocalDateTime) {
        val delay = Duration.between(LocalDateTime.now(), dateTime).seconds

        val data = workDataOf("title" to title)

        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(10),
            )
            .setInputData(data)
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniqueWork(
            title,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    fun scheduleDailyReminder(
        context: Context,
        title: String,
        time: LocalTime
    ) {
        val now = LocalTime.now()

        // Calcular el retraso hasta la próxima ocurrencia de esa hora
        val delay = if (time.isAfter(now)) {
            Duration.between(now, time).seconds
        } else {
            Duration.between(now, time.plusHours(24)).seconds
        }

        val data = workDataOf("title" to title)

        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(1, TimeUnit.MINUTES)
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setBackoffCriteria(
                backoffPolicy = BackoffPolicy.LINEAR,
                duration = Duration.ofSeconds(10),
            )
            .setInputData(data)
            //.setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()

        val workManager = WorkManager.getInstance(context)

        workManager.enqueueUniquePeriodicWork(
            title,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

    }

}