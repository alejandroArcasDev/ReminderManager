package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.presentation.components.CustomTopAppBar
import com.alejandroarcas.reminder_manager.reminder.presentation.components.DateTimePicker
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderDetailScreen(id: Int, detailViewModel: ReminderDetailViewModel, navigateBack: () -> Unit) {

    val reminderDetailState by detailViewModel.reminderDetailState.collectAsStateWithLifecycle()
    val reminder by detailViewModel.reminder.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(true) {

        detailViewModel.onAction(ReminderDetailActions.GetReminder(id))

        // Reminder Updated
        detailViewModel.reminderSavedChannel.collectLatest { updated ->
            if (updated) {
                detailViewModel.onAction(ReminderDetailActions.GetReminder(id))
                Toast.makeText(
                    context,
                    "Reminder updated",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Error updating reminder",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(topBar = {
        CustomTopAppBar(title = "Reminder Detail", navigateBack = navigateBack)
    }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (reminderDetailState.isLoading) {
                CircularProgressIndicator()
            } else {
                // Title
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (!reminderDetailState.isTitleEditing) {
                        Text(
                            text = reminderDetailState.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    } else {
                        TextField(
                            value = reminderDetailState.title,
                            onValueChange = {
                                detailViewModel.onAction(ReminderDetailActions.UpdateTitle(it))
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                disabledContainerColor = MaterialTheme.colorScheme.background,
                                disabledIndicatorColor = Color.Gray,
                                focusedContainerColor = MaterialTheme.colorScheme.background,
                                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        modifier = Modifier
                            .background(
                                color = if (!reminderDetailState.isTitleEditing) Color.LightGray else Color.Green,
                                shape = CircleShape
                            ),
                        onClick = {
                            detailViewModel.enableEdit(field = "title")
                            if (reminderDetailState.isTitleEditing) {
                                detailViewModel.onAction(
                                    ReminderDetailActions.UpdateTitle(
                                        reminderDetailState.title
                                    )
                                )
                            }
                        }) {
                        Icon(
                            imageVector = if (!reminderDetailState.isTitleEditing) Icons.Default.Edit else Icons.Default.Check,
                            contentDescription = "Edit"
                        )
                    }
                }

                // Date
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    DateTimePicker(
                        interval = reminderDetailState.interval,
                        onDateTimeSelected = { dateTime ->
                            Log.d("DateTimePicker", "Fecha y hora seleccionada: $dateTime")
                        }
                    )
                }

                // Interval
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = reminderDetailState.isDropdownExpanded,
                        onExpandedChange = { detailViewModel.toggleDropdown() }) {
                        OutlinedTextField(
                            value = reminderDetailState.interval.toString().lowercase()
                                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = reminderDetailState.isDropdownExpanded)
                            },

                            placeholder = {
                                Text(text = "Select the interval")
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = ExposedDropdownMenuDefaults.textFieldColors(
                                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                                unfocusedIndicatorColor = Color.Gray,
                                disabledContainerColor = MaterialTheme.colorScheme.background,
                                disabledIndicatorColor = Color.Gray,
                                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier
                                .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                                .fillMaxWidth(0.6f)
                        )
                        ExposedDropdownMenu(
                            expanded = reminderDetailState.isDropdownExpanded,
                            onDismissRequest = { detailViewModel.toggleDropdown() }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(text = "Once")
                                },
                                onClick = {
                                    detailViewModel.onAction(
                                        ReminderDetailActions.UpdateInterval(
                                            Interval.ONCE
                                        )
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(text = "Daily")
                                },
                                onClick = {
                                    detailViewModel.onAction(
                                        ReminderDetailActions.UpdateInterval(
                                            Interval.DAILY
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                // Save button
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (reminderDetailState.title != reminder!!.title || reminderDetailState.interval != reminder!!.interval) {
                        Button(
                            modifier = Modifier.fillMaxWidth(0.70f),
                            onClick = {
                            detailViewModel.onAction(ReminderDetailActions.UpdateReminder)
                        }) {
                            Text(text = "Update reminder")
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save"
                            )

                        }
                    }
                }
                // Delete button
                Row(
                    modifier = Modifier.fillMaxWidth().padding(4.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(0.70f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            detailViewModel.onAction(
                                ReminderDetailActions.DeleteReminder(reminder!!)
                            )
                            navigateBack()
                        }) {
                        Text(text = "Delete", color = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}