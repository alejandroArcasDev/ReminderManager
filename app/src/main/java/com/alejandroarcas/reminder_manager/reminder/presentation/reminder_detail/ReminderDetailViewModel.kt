package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail.model.ReminderDetailUiState
import com.alejandroarcas.reminder_manager.reminder.use_cases.DeleteReminder
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetReminder
import com.alejandroarcas.reminder_manager.reminder.use_cases.UpsertReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ReminderDetailViewModel@Inject constructor(
    private val getReminder: GetReminder,
    private val deleteReminder: DeleteReminder,
    private val upsertReminder: UpsertReminder
) : ViewModel() {


    private val _reminderDetailUiState = MutableStateFlow(ReminderDetailUiState())
    val reminderDetailState = _reminderDetailUiState.asStateFlow()

    private val _reminder = MutableStateFlow<Reminder?>(null)
    val reminder = _reminder.asStateFlow()

    private val _reminderSavedChannel = Channel<Boolean>()
    val reminderSavedChannel = _reminderSavedChannel.receiveAsFlow()

    private val _reminderDeletedChannel = Channel<Boolean>()
    val reminderDeletedChannel = _reminderDeletedChannel.receiveAsFlow()


    fun onAction(action: ReminderDetailActions) {
        when (action) {
            is ReminderDetailActions.GetReminder -> {
                _reminderDetailUiState.update {
                    it.copy(isLoading = true)
                }
                viewModelScope.launch {
                    getReminder(action.id)
                }
            }

            is ReminderDetailActions.DeleteReminder -> {
                viewModelScope.launch {
                    deleteReminder(action.reminder)
                    _reminderDeletedChannel.send(true)
                }
            }

            is ReminderDetailActions.UpdateTitle -> {
                _reminderDetailUiState.update {
                    it.copy(title = action.title)
                }
            }

            is ReminderDetailActions.UpdateInterval -> {
                _reminderDetailUiState.update {
                    it.copy(interval = action.interval)
                }

            }

            is ReminderDetailActions.UpdateDateTime -> {
                _reminder.value?.let {
                    _reminder.value = it.copy(dateTime = action.dateTime,
                        time = action.dateTime.toLocalTime())
                }
                _reminderDetailUiState.update {
                    it.copy(isDateTimeEditing = true)
                }
            }

            is ReminderDetailActions.UpdateReminder -> {
                _reminderDetailUiState.update {
                    it.copy(isLoading = true)
                }
                viewModelScope.launch {
                    val isUpdated = upsertReminder(
                        id = reminder.value!!.id,
                        title = reminderDetailState.value.title,
                        interval = reminderDetailState.value.interval,
                        dateTime = reminder.value!!.dateTime,
                        active = reminder.value!!.active
                    )
                    _reminderSavedChannel.send(isUpdated)

                }
            }

        }
    }

    /**
     * Retrieves the reminder with the given id and
     * updates [_reminderDetailUiState] and [_reminder]
     * with the reminder data.
     * @param id The id of the reminder to retrieve.
     */
    private suspend fun getReminder(id: Int) {
        val reminder = getReminder.invoke(id)
        _reminderDetailUiState.update {
            it.copy(
                title = reminder.title,
                interval = reminder.interval,
                isLoading = false,
                isActive = reminder.active
            )
        }
        _reminder.value = reminder
    }

    /**
     * Updates or inserts a reminder with the given title and interval.
     * @param title The title of the reminder.
     * @param interval The interval of the reminder.
     * @return True if the reminder was updated or inserted, false otherwise.
     */
    suspend fun upsertReminder(title: String, interval: Interval, dateTime: LocalDateTime?): Boolean {
        return upsertReminder.invoke(
            id = reminder.value!!.id,
            title = title,
            interval = interval,
            dateTime = dateTime,
            active = reminder.value!!.active
        )
    }

    /**
     * Deletes the given reminder.
     * @param reminder The reminder to delete.
     */
    private suspend fun deleteReminder(reminder: Reminder) {
        deleteReminder.invoke(reminder)
    }


    fun enableEdit(field: String){
        _reminderDetailUiState.update {
            when(field){
                "title" -> it.copy(isTitleEditing = !it.isTitleEditing)
                "interval" -> it.copy(isIntervalEditing = !it.isIntervalEditing)
                else -> it
            }
        }
    }

    fun toggleDropdown(){
        _reminderDetailUiState.update {
            it.copy(isDropdownExpanded = !it.isDropdownExpanded)
        }
    }

    fun deactivateReminder(context: Context, title: String) {
        WorkManager.getInstance(context).cancelUniqueWork(title)
    }


}