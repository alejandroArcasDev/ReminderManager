package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model.ReminderListUiState
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetAllReminders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllReminders
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReminderListUiState>(ReminderListUiState.LOADING)
    val uiState: StateFlow<ReminderListUiState> = _uiState

    fun loadReminderList() {
        _uiState.value = ReminderListUiState.LOADING
        viewModelScope.launch {
            val reminders = getAllRemindersUseCase()
            if (reminders.isEmpty()) {
                _uiState.value = ReminderListUiState.EMPTY
            } else {
                _uiState.value = ReminderListUiState.SUCCESS(reminders)
            }
        }
    }
}