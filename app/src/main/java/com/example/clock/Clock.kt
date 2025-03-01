package com.example.clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale
import android.content.Context
import android.widget.TimePicker
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.Calendar
import androidx.compose.material3.TimePicker

@Composable
fun Alar(alarmManager: Alarm){
    var tipicker by remember { mutableStateOf(false) }
    val Lalarms by remember { mutableStateOf(alarmManager.getAlarms()) }
    Box() {
//        var alarmlist by remember { mutableListOf() }
        val context = LocalContext.current
        val alarmManager = Alarm(context)
        Column(
            Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
            Button(onClick = {tipicker=true}, colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White
            ), shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            } }
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            var current by remember { mutableStateOf(getCurrentTime()) }

            LaunchedEffect(Unit) {
                while (true) {
                    current = getCurrentTime()
                    delay(1000) // Update every second
                }
            }
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Red, fontSize = 55.sp)) {
                        append(current.first().toString()) // Convert char to String
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 50.sp
                        )
                    ) {
                        append(current.drop(1)) // Append the rest of the time
                    }
                }
            )
//        isko hi implement karna hai bs sahi se
//        Button(onClick = {
//            alarmManager.scheduleAlarm()
//        }) {
//            Text(text = "Set Alarm")
//        }
//        Spacer(Modifier.height(30.dp))
//        Button(onClick = {
//            alarmManager.cancelAlarm()
//        }) {
//            Text(text = "Cancel Alarm")
//        }


        }
        Column(Modifier.fillMaxSize(),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
            if (tipicker) {
                DialExample(context,onConfirm = { hour, minute ->
                    val randomRequestCode = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
                    alarmManager.scheduleAlarm(hour,minute,randomRequestCode)
                }, onDismiss = {
                    tipicker = false
                })
            }
        }

    }
}
fun getCurrentTime() :String{
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

}
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun DialExample(context: Context,
    onDismiss: ()->Unit,
    onConfirm: (Int, Int) -> Unit // Pass selected hour & minute
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        TimePicker(
            state = timePickerState,
        )
        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceEvenly){
            Button(onClick = onDismiss) { Text("cancel") }
            Button(onClick = {
                onConfirm(timePickerState.hour, timePickerState.minute)// Send selected time
                Toast.makeText(context,"alarm sheduled",Toast.LENGTH_LONG).show()
                onDismiss()
            }) {
                Text("Confirm")
            }
        }
    }
}
