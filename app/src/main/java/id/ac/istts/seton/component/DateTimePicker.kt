package id.ac.istts.seton.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomDatePicker(
    dateFormat: String = "MM/dd/yyyy",
    state: DatePickerState,
    onDatePicked: (() -> Unit),
    modifier: Modifier = Modifier
) {
    val displayDate = remember {
        mutableStateOf(TFDateUtils.getCurrentDateString(dateFormat))
    }
    val openDialog = remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    if (interactionSource.collectIsPressedAsState().value) {
        openDialog.value = true
    }
    val confirmEnabled = remember {
        derivedStateOf { state.selectedDateMillis != null }
    }

    OutlinedTextField(
        value = displayDate.value,
        onValueChange = {},
        interactionSource = interactionSource,
        label = { Text(text = "Date") },
        trailingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Time") },
        singleLine = true,
        modifier = modifier
    )

    if (openDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (state.selectedDateMillis != null) {
                            displayDate.value =
                                TFDateUtils.convertMillisToDateString(
                                    millis = state.selectedDateMillis!!,
                                    format = dateFormat
                                )
                            openDialog.value = false
                            onDatePicked()
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomTimePicker(
    timeFormat: String = "hh:mm a",
    state: TimePickerState,
    onTimePicked: (() -> Unit),
    modifier: Modifier
) {
    val timeInteractionSource = remember { MutableInteractionSource() }
    val displayTime = remember {
        mutableStateOf(TFDateUtils.getCurrentTimeString(timeFormat))
    }
    val showTimePicker = remember { mutableStateOf(false) }
    if (timeInteractionSource.collectIsPressedAsState().value) {
        showTimePicker.value = true
    }
    val confirmTimePickerEnabled = remember {
        derivedStateOf { state.hour != null && state.minute != null }
    }
    OutlinedTextField(
        value = displayTime.value,
        onValueChange = {},
        interactionSource = timeInteractionSource,
        label = { Text(text = "Time") },
        trailingIcon = { Icon(imageVector = Icons.Default.Create, contentDescription = "Time") },
        singleLine = true,
        modifier = modifier
    )

    if (showTimePicker.value) {
        TimePickerDialog(
            onDismissRequest = {
                showTimePicker.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker.value = false
                        displayTime.value = TFDateUtils.getTimeString(
                            hour = state.hour,
                            min = state.minute
                        )
                        onTimePicked()
                    },
                    enabled = confirmTimePickerEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showTimePicker.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            TimePicker(state = state)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomDateTimePicker(
    title: String,
    dateFormat: String = "MM/dd/yyyy",
    timeFormat: String = "hh:mm a",
    state: MutableState<LocalDateTime>,
    modifier: Modifier = Modifier
) {
    val dateState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    val dateTimeFormat = "$dateFormat $timeFormat"

    val updateState = {
        val dateString = if (dateState.selectedDateMillis != null) {
            TFDateUtils.convertMillisToDateString(
                millis = dateState.selectedDateMillis!!,
                format = dateFormat
            )
        } else {
            TFDateUtils.getCurrentDateString(dateFormat)
        }
        val timeString = if (timePickerState.minute != null && timePickerState.hour != null) {
            TFDateUtils.getTimeString(
                hour = timePickerState.hour,
                min = timePickerState.minute
            )
        } else {
            TFDateUtils.getCurrentTimeString(timeFormat)
        }
        state.value = TFDateUtils.getLocalDataTimeFromString(
            dateTime = "$dateString $timeString",
            format = dateTimeFormat
        )
    }

    Column(modifier = modifier) {
        Text(
            text = title,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row {
            CustomDatePicker(
                dateFormat = dateFormat,
                state = dateState,
                onDatePicked = updateState,
                modifier = Modifier
                    .weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))

            CustomTimePicker(
                state = timePickerState,
                onTimePicked = updateState,
                modifier = Modifier
                    .weight(.5f)
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable (() -> Unit),
    dismissButton: @Composable (() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = containerColor
                ),
            color = containerColor
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    text = title,
                    style = MaterialTheme.typography.labelMedium
                )
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    dismissButton?.invoke()
                    confirmButton()
                }
            }
        }
    }
}