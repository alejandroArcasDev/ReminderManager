package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.use_cases.UpsertReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    private val addReminderUseCase: UpsertReminder,
) : ViewModel() {

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet = _showBottomSheet.asStateFlow()

    private val _reminderAddedChannel = Channel<Boolean>()
    val reminderAddedChannel = _reminderAddedChannel.receiveAsFlow()

    fun showBottomSheet(value: Boolean) {
        _showBottomSheet.value = value
    }

    fun addReminder(title: String, interval: Interval) {
        viewModelScope.launch {
            val added = addReminderUseCase(id = 0, title = title, interval = interval)
            _reminderAddedChannel.send(added)
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
    }

}