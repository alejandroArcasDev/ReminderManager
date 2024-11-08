package com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alejandroarcas.reminder_manager.reminder.domain.mapper.toPresentation
import com.alejandroarcas.reminder_manager.reminder.domain.model.Interval
import com.alejandroarcas.reminder_manager.reminder.domain.model.Reminder
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_detail.ReminderDetailViewModel
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.components.AddReminderBottomSheet
import com.alejandroarcas.reminder_manager.reminder.presentation.reminder_list.model.ReminderListUiState
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter

@Composable
fun ReminderListScreen(listViewModel: ListViewModel, addViewModel: AddViewModel, detailViewModel: ReminderDetailViewModel, navigateToDetail: (Int) -> Unit) {

    val uiState by listViewModel.uiState.collectAsStateWithLifecycle()

    val showBottomSheet by addViewModel.showBottomSheet.collectAsStateWithLifecycle()


    if (showBottomSheet){
        AddReminderBottomSheet(addViewModel)
    }

    LaunchedEffect(true) {
        listViewModel.loadReminderList()

        addViewModel.reminderAddedChannel.collectLatest { added ->
            if (added) {
                listViewModel.loadReminderList()
            }
        }

        detailViewModel.reminderSavedChannel.collectLatest { updated ->
            if (updated) {
                listViewModel.loadReminderList()
            }
        }
    }

    when (val state = uiState) {
        is ReminderListUiState.LOADING -> ListLoading()
        is ReminderListUiState.SUCCESS -> ListSuccess(state.list, addViewModel, navigateToDetail)
        is ReminderListUiState.EMPTY -> ListEmpty(addViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListEmpty(addViewModel: AddViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Reminders") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addViewModel.showBottomSheet(true)
                    addViewModel.resetBottomSheetValues()
                },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                elevation = FloatingActionButtonDefaults.elevation(1.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add", modifier = Modifier.size(35.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Rounded.List, contentDescription = "Empty")
            Text("No reminders yet")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListSuccess(reminderList: List<Reminder>, addViewModel: AddViewModel, navigateToDetail: (Int) -> Unit) {

    val listOfItemColors = listOf(Color(0xFF5fb995), Color(0xFF5fb8b9), Color(0xFF5f85b9))

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Reminders") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addViewModel.showBottomSheet(true)
                    addViewModel.resetBottomSheetValues()
                },
                shape = CircleShape,
                modifier = Modifier.size(80.dp),
                elevation = FloatingActionButtonDefaults.elevation(1.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add", modifier = Modifier.size(35.dp))
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(reminderList.size) {
                    ReminderItem(reminderList[it], listOfItemColors.random(), navigateToDetail)
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
fun ReminderItem(reminder: Reminder, randomColor: Color, navigateToDetail: (Int) -> Unit) {

    val dateText = when (reminder.interval){
        Interval.ONCE -> {
            reminder.dateTime!!.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        }

        Interval.DAILY -> {
            reminder.time
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(0.96f)
            .height(150.dp)
            .background(color = randomColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        Text(reminder.title, fontSize = 24.sp, fontWeight = FontWeight.Medium)
        Row {
            Icon(Icons.Rounded.DateRange, contentDescription = "Date")
            Spacer(Modifier.width(5.dp))
            Text(dateText.toString())
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(reminder.interval.toPresentation())
            Row(
                Modifier.clickable {
                    navigateToDetail(reminder.id)
                }
            ) {
                Text("View details")
                Spacer(Modifier.width(5.dp))
                Icon(Icons.Rounded.ArrowForward, contentDescription = "Date")
            }
        }
    }
}

@Composable
fun ListLoading() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(Modifier.height(5.dp))
            Text("Loading...")
        }
    }
}
