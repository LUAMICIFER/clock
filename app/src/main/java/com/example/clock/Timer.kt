package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.commandiron.wheel_picker_compose.WheelTimePicker
import com.commandiron.wheel_picker_compose.core.WheelPickerDefaults
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import kotlinx.coroutines.delay
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import java.time.LocalTime.*

@Composable
fun Timer(viewModel: TimerviewModel = viewModel()){
    val selectedTime by viewModel.selectedtime.observeAsState()
    val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
    val isTimerRunning by viewModel.istimerrunning.observeAsState(initial = false)
    val Progres by viewModel.progress.observeAsState(initial = 0f)
    val context = LocalContext.current

    val totalTimeInSec = ((selectedTime?.hour ?: 0) * 60 * 60) +((selectedTime?.minute ?: 0) * 60)
    val playpause by viewModel.playPause.observeAsState(initial = true)
    Box(Modifier.fillMaxSize()) {
        Column {
            Spacer(Modifier.height(20.dp))

            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.Red, fontSize = 55.sp)) {
                        append("T") // Convert char to String
                    }
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground, fontSize = 50.sp)) {
                        append("imer") // Append the rest of the (imer)
                    }
                }
            )
        }
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)  {

            if(!isTimerRunning) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement =Arrangement.SpaceEvenly) {
                    Text("H", fontSize = 32.sp)
                    Text(":", fontSize = 32.sp)
                    Text("M", fontSize = 32.sp)
                }
                WheelTimePicker(
                    textColor = MaterialTheme.colorScheme.onBackground,
                    startTime = viewModel.selectedtime.value?: MIDNIGHT,
                    size = DpSize(400.dp, 250.dp),
                    textStyle = MaterialTheme.typography.titleLarge,
                    rowCount = 5, selectorProperties = WheelPickerDefaults.selectorProperties(
                        color = MaterialTheme.colorScheme.background,
                        border = null
                    )
                ) { snappedTime ->
                    viewModel._setselectedtime(snappedTime)
                }
                Spacer(Modifier.height(30.dp))
                TextField(
                    value = selectedTime?.format(timeFormatter) ?: LocalTime.MIDNIGHT.format(timeFormatter),
                    onValueChange = { newTime: String ->
                        try {
                            val parsedTime = LocalTime.parse(newTime, timeFormatter)
                            viewModel._setselectedtime(parsedTime) // Corrected function call
                        } catch (e: Exception) {
                            // Handle error if needed
                        }
                    },
                    readOnly = true,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(0.6f)
                        .clip(RoundedCornerShape(16.dp))
                )

                Spacer(Modifier.height(30.dp))
                IconButton(
                    onClick = { viewModel._setisTimerRunning(true) },
                    Modifier
                        .width(130.dp)
                        .height(60.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHighest,
                            shape = RoundedCornerShape(32.dp)
                        )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "play",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            else{

                LaunchedEffect(Progres, isTimerRunning, playpause) {
                    while (Progres < totalTimeInSec && isTimerRunning && playpause) {
                        delay(1000)
                        viewModel._setprogress(Progres+1f)
                    }
                    if (Progres >= totalTimeInSec) {
                         viewModel._setisTimerRunning(false)
//                        TimerManager.playSound(context)
                    }
                }
                CircularProgressBar(
                    modifier = Modifier.size(400.dp),
                    progress = Progres,
                    progressMax = totalTimeInSec.toFloat(),
                    progressBarColor = Color.Blue,
                    progressBarWidth = 18.dp,
                    backgroundProgressBarColor = Color.Gray,
                    backgroundProgressBarWidth = 18.5.dp,
                    roundBorder = true,
                    startAngle = 360f
                )
                Spacer(Modifier.height(30.dp))
                ////////////////////////////
                Text("Total Time: ${selectedTime?.hour}:${selectedTime?.minute}:${selectedTime?.second}", fontSize = 32.sp)
                Text("Remaining Time: ${(totalTimeInSec-Progres.toInt())/60}:${(totalTimeInSec-Progres.toInt())%60}", fontSize = 32.sp)
                Spacer(Modifier.height(30.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Button(onClick = {
                        viewModel._setPlayPause(!playpause)
                    }) { Text(text=if(playpause) {
                        "pause"
                    }
                    else{
                        "play"
                    }) }
                    Button(onClick = {
                        viewModel._setisTimerRunning(false)
                        viewModel._setprogress(0f)
                        viewModel._setPlayPause(!playpause)
                    }) { Text("Stop") }
                }
            }
        }


    }

}
@Preview(showBackground = true)
@Composable
fun TimerPreview(){
    Timer()
}
