package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model.ReminderListUiState
import com.alejandroarcas.reminder_manager.reminder.use_cases.GetAllReminders
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllRemindersUseCase: GetAllReminders
) : ViewModel() {

    private val _uiState = MutableStateFlow<ReminderListUiState>(ReminderListUiState.LOADING)
    val uiState: StateFlow<ReminderListUiState> = _uiState

    init {
        loadReminderList()
    }

    private fun loadReminderList() {
        _uiState.value = ReminderListUiState.LOADING
        viewModelScope.launch {
            val reminders = listOf(
                Reminder(1, "Reminder", Interval.ONCE),
                Reminder(2, "Reminder 2", Interval.DAILY),
                Reminder(3, "Reminder 3", Interval.DAILY),
                Reminder(4, "Reminder 4", Interval.DAILY),
                Reminder(5, "Reminder 5", Interval.DAILY),
                Reminder(6, "Reminder 6", Interval.DAILY),
                Reminder(7, "Reminder 7", Interval.DAILY),
                Reminder(8, "Reminder 8", Interval.DAILY),
                Reminder(9, "Reminder 9", Interval.DAILY),
                Reminder(10, "Reminder 10", Interval.DAILY),
            )

            if (reminders.isEmpty()) {
                _uiState.value = ReminderListUiState.EMPTY
            } else {
                _uiState.value = ReminderListUiState.SUCCESS(reminders)
            }
        }
    }


}