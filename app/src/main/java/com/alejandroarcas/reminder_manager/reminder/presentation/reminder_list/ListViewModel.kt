package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkManager
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model.ReminderListUiState
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetAllReminders
import com.alejandroarcas.reminder_manager.reminder.use_cases.UpsertReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllReminders,
    private val upsertReminder: UpsertReminder
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReminderListUiState>(ReminderListUiState.LOADING)
    val uiState: StateFlow<ReminderListUiState> = _uiState

    private val _remindersList = MutableStateFlow<List<Reminder>>(emptyList())
    val remindersList: StateFlow<List<Reminder>> = _remindersList

    fun loadReminderList() {
        _uiState.value = ReminderListUiState.LOADING
        viewModelScope.launch {
            val reminders = getAllRemindersUseCase()
            if (reminders.isEmpty()) {
                _uiState.value = ReminderListUiState.EMPTY
            } else {
                _uiState.value = ReminderListUiState.SUCCESS
                _remindersList.value = reminders
            }
        }
    }

    fun deactivateReminder(context: Context, title: String) {
        WorkManager.getInstance(context).cancelUniqueWork(title)
    }

    fun changeReminderStatus(reminder: Reminder){
        viewModelScope.launch {
            val updatedReminder = reminder.copy(active = !reminder.active)
            upsertReminder(updatedReminder)

            _remindersList.value = _remindersList.value.map {
                if (it.id == reminder.id) updatedReminder else it
            }
        }
    }
}