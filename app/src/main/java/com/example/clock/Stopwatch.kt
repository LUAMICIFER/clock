package com.example.clock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Duration
import java.time.LocalTime

data class FlagDuration(var startTime: Duration, var endTime: Duration)

@Composable
fun Stopwatch() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        // Stopwatch Title
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Red, fontSize = 55.sp)) {
                    append("S")
                }
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 50.sp
                    )
                ) {
                    append("topwatch")
                }
            }
        )

        var isPlaying by remember { mutableStateOf(false) }
        var flag by remember { mutableStateOf(false) }
        val distance by remember {
            derivedStateOf { if (!flag) 300 else 50 }
        }
        var timeList by remember { mutableStateOf(listOf<FlagDuration>()) }
        var startTime by remember { mutableStateOf(LocalTime.now()) }
        var runningTime by remember { mutableStateOf(LocalTime.MIDNIGHT) }
        Spacer(Modifier.height(distance.dp))

        val duration = Duration.between(startTime, runningTime).abs()
        val hours = duration.toHours().toInt()
        val minutes = (duration.toMinutes() % 60).toInt()
        val seconds = (duration.seconds % 60).toInt()
        val nano = (duration.nano / 10_000_000) % 100

        if (isPlaying) {
            Text("$hours:$minutes:$seconds.$nano", fontSize = 32.sp)

            startTime = LocalTime.now()
            LaunchedEffect(isPlaying) {
                runningTime = LocalTime.now()
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = {
                    flag = true
                    if (timeList.isEmpty()) {
                        timeList = timeList + FlagDuration(Duration.ZERO, duration)
                    } else {
                        val lastFlagEndTime = timeList.last().endTime
                        val newFlagDuration = duration.minus(lastFlagEndTime)
                        timeList = timeList + FlagDuration(lastFlagEndTime, lastFlagEndTime.plus(newFlagDuration))
                    }
                }) {
                    Text("Flag")
                }
                Button(onClick = { isPlaying = !isPlaying
                    timeList = emptyList()
                    flag = !flag
                }) {

                    Text("Stop")
                }
            }
        } else {
            Text("00:00:00.00", fontSize = 32.sp)
            Spacer(Modifier.height(if (flag) 550.dp else 250.dp))
            Button(onClick = { isPlaying = !isPlaying }) {
                Text("Play")
            }
        }

        Spacer(Modifier.height(20.dp))

        // LazyColumn to display flagged times
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(timeList) { flag ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        text = "Start: ${flag.startTime.toMinutes()} min ${flag.startTime.seconds % 60} sec",
                        fontSize = 16.sp
                    )
                    Text(
                        text = "End: ${flag.endTime.toMinutes()} min ${flag.endTime.seconds % 60} sec",
                        fontSize = 16.sp
                    )
                    Divider(color = Color.Gray, thickness = 1.dp)
                }
            }
        }
    }
}
