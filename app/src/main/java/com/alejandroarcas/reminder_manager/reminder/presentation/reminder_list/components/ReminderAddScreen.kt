package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.presentation.components.DateTimePicker
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.AddViewModel
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReminderBottomSheet(addViewModel: AddViewModel) {
    val title by addViewModel.title.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    val onceChip by addViewModel.onceChip.collectAsStateWithLifecycle()
    val dailyChip by addViewModel.dailyChip.collectAsStateWithLifecycle()
    val dateTime by addViewModel.dateTime.collectAsStateWithLifecycle()

    val context = LocalContext.current

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            addViewModel.showBottomSheet(false)
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(10.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(50.dp)
                    .padding(start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(10.dp),
                value = title,
                onValueChange = { newTitle ->
                    addViewModel.onTitleChange(newTitle)
                },
                singleLine = true,
                placeholder = { Text(text = "Enter a reminder name...") },
            )
            Spacer(Modifier.height(25.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ElevatedFilterChip(
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Once",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    selected = onceChip,
                    onClick = { addViewModel.updateChipOnClick("once") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF5f85b9),
                        containerColor = Color.LightGray,
                        labelColor = Color.Gray
                    )
                )
                ElevatedFilterChip(
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp),
                    label = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Daily",
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    },
                    selected = dailyChip,
                    onClick = { addViewModel.updateChipOnClick("daily") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF5f85b9),
                        containerColor = Color.LightGray,
                        labelColor = Color.Gray
                    )
                )
            }
            Spacer(Modifier.height(25.dp))

            // Date
            Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                DateTimePicker(
                    interval = if(onceChip) Interval.ONCE else Interval.DAILY,
                    dateTime = if(onceChip) LocalDateTime.now() else null,
                    time =  if(dailyChip) dateTime.toLocalTime()?: LocalTime.now() else null,
                    onDateTimeSelected = { dateTimePicked ->
                        Log.d("DateTimePicker", "Fecha y hora seleccionada: $dateTimePicked")
                        addViewModel.setDate(dateTimePicked)
                    }
                )
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
                    .padding(start = 10.dp, end = 10.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5f85b9)
                ),
                onClick = {
                    addViewModel.addReminder(context, title, addViewModel.chipValueToUseCase(), dateTime)
                    addViewModel.showBottomSheet(false)
                }
            ) { Text("Add reminder", color = Color.White, fontSize = 16.sp) }
            Spacer(Modifier.height(10.dp))
        }


    }
}