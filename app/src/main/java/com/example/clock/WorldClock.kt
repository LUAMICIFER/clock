package com.example.clock

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WorldClock(){
//    Text("Worldclock Screen")
    var showlist = remember { mutableStateListOf<String>() }
    Box(){
        var allList by rememberSaveable{ mutableStateOf(false) }

        if(!allList){
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(bottom = 10.dp, end = 10.dp), verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.End) {
                Button(onClick = {allList=true}, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Green,
                    contentColor = Color.White
                ), shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                } }

            Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(Modifier.height(40.dp))
//            Spacer(Modifier.height(20.dp))

                Text(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Red, fontSize = 55.sp)) {
                            append("W") // Convert char to String
                        }
                        withStyle(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 50.sp
                            )
                        ) {
                            append("orldClock") // Append the rest of the (imer)
                        }
                    }
                )
                AnalogClock()
                LazyColumn(contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(showlist){zone->

                        var temp by rememberSaveable { mutableStateOf(getTimeByZone(zone)) }
                            Column(
                                Modifier.clip(RoundedCornerShape(16.dp))
                                    .fillMaxWidth().background(Color(0xFF424242))
                                    ) {
                                Row(Modifier.fillMaxWidth(),horizontalArrangement = SpaceBetween) {
                                    Text(text = zone, fontSize = 16.sp)
                                    LaunchedEffect(Unit){
                                        while (true) {
                                        delay(60000)
                                        temp= getTimeByZone(zone)}
                                    }
                                    Text(text =temp, fontSize = 32.sp)
                                }
                                Text(text = "${getDateByZone(zone)} | ${getz(zone)}")
                            }


                    }
                }


            }
        }
        else{
//            val zonelist=ZoneId.getAvailableZoneIds().toList()
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                Row {Button(onClick = {allList=false}, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ), shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back")
                }  }
                Text("Select cities", color = Color.White, fontSize = 32.sp)
                Text("Time Zones",color= Color.Gray, fontSize = 16.sp)
                var searchQuery by remember { mutableStateOf("") } // Stores user input
                val zoneList = remember { ZoneId.getAvailableZoneIds().toList() } // Get all time zones
                val filteredList = zoneList.filter { it.contains(searchQuery, ignoreCase = true) } // Filter list

                Column(Modifier.padding(16.dp)) {
                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text("Search Zone") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(12.dp)) // Add spacing

                    // List of Zones
                    LazyColumn {
                        items(filteredList) { zoneId ->
                            Text(
                                text = zoneId,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { showlist.add(zoneId)
                                    allList=false}
                            )
                        }
                    }
                }


            }

        }

    }
}
@Composable
fun AnalogClock() {
    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    val textMeasurer = rememberTextMeasurer()
    var circleCenter by remember { mutableStateOf(Offset.Zero) }
    val circleRadius = 450f

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = System.currentTimeMillis()
        }
    }

    Box(
        modifier = Modifier
            .size(350.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            circleCenter = Offset(x = width / 2f, y = height / 2f)

            val date = Date(currentTime)
            val calendar = Calendar.getInstance()
            calendar.time = date
            val hours = calendar.get(Calendar.HOUR_OF_DAY)
            val minutes = calendar.get(Calendar.MINUTE)
            val seconds = calendar.get(Calendar.SECOND)

            val brush = Brush.radialGradient(
                listOf(
                    Color(0xFF212121).copy(0.45f),
                    Color(0xFF424242).copy(0.35f),
                    Color(0xFF616161).copy(0.45f)
                )
            )

            // Draw Clock Border
            drawCircle(
                style = Stroke(width = 15f),
                brush = brush,
                radius = circleRadius + 7f,
                center = circleCenter
            )
            // Draw Clock Background
            drawCircle(
                brush = brush,
                radius = circleRadius,
                center = circleCenter
            )

            val littleLineLength = circleRadius * 0.08f
            val largerLineLength = circleRadius * 0.11f
            val textCircleRadius = circleRadius - 80f

            for (i in 0 until 60) {
                val angleInDegree = i * 360f / 60
                val angleInRad = Math.toRadians(angleInDegree.toDouble())

                val lineLength = if (i % 5 == 0) largerLineLength else littleLineLength
                val lineThickness = if (i % 5 == 0) 5f else 2f
                val lineColor = if (i % 5 == 0) Color.White else Color.Gray

                val start = Offset(
                    x = (circleCenter.x + (circleRadius - lineLength) * cos(angleInRad)).toFloat(),
                    y = (circleCenter.y + (circleRadius - lineLength) * sin(angleInRad)).toFloat()
                )
                val end = Offset(
                    x = (circleCenter.x + circleRadius * cos(angleInRad)).toFloat(),
                    y = (circleCenter.y + circleRadius * sin(angleInRad)).toFloat()
                )

                drawLine(
                    color = lineColor,
                    start = start,
                    end = end,
                    strokeWidth = lineThickness,
                    cap = StrokeCap.Round
                )

                if (i % 5 == 0) {
                    val number = if (i == 0) 12 else i / 5
                    val textAngle = angleInDegree - 90f
                    val textOffset = Offset(
                        x = (cos(Math.toRadians(textAngle.toDouble())) * textCircleRadius + circleCenter.x).toFloat(),
                        y = (sin(Math.toRadians(textAngle.toDouble())) * textCircleRadius + circleCenter.y).toFloat()
                    )

                    val textLayoutResult = textMeasurer.measure(
                        text = number.toString(),
                        style = TextStyle.Default.copy(fontSize =  20.sp, color = Color.White)
                    )
                    drawText(
                        textLayoutResult,
                        topLeft = Offset(
                            textOffset.x - textLayoutResult.size.width / 2,
                            textOffset.y - textLayoutResult.size.height / 2
                        )
                    )
                }
            }
            val clockHands = listOf(ClockHands.Seconds, ClockHands.Minutes, ClockHands.Hours)
            clockHands.forEach { clockHand ->
                val angleInDegree = when (clockHand) {
                    ClockHands.Seconds -> seconds * 6f  // 360/60 = 6° per second
                    ClockHands.Minutes -> minutes * 6f + (seconds / 60f) * 6f  // 6° per minute
                    ClockHands.Hours -> (hours % 12) * 30f + (minutes / 60f) * 30f  // 30° per hour
                }

                val lineThickness = when (clockHand) {
                    ClockHands.Seconds -> 3f
                    ClockHands.Minutes -> 7f
                    ClockHands.Hours -> 9f
                }

                val lineLength = when (clockHand) {
                    ClockHands.Seconds -> circleRadius * 0.8f
                    ClockHands.Minutes -> circleRadius * 0.7f
                    ClockHands.Hours -> circleRadius * 0.5f
                }

                val end = Offset(
                    x = (circleCenter.x + lineLength * cos(Math.toRadians(angleInDegree - 90.0))).toFloat(),
                    y = (circleCenter.y + lineLength * sin(Math.toRadians(angleInDegree - 90.0))).toFloat()
                )

                drawLine(
                    color = if (clockHand == ClockHands.Seconds) Color.White else Color.Gray,
                    start = circleCenter,
                    end = end,
                    strokeWidth = lineThickness,
                    cap = StrokeCap.Round
                )
            }
        }
    }

}
enum class ClockHands{
    Seconds,
    Minutes,
    Hours
}
@Preview
@Composable
fun hellPreview(){
    WorldClock()
}

fun getDateByZone(zoneId: String): String {
    val zonedDateTime = ZonedDateTime.now(ZoneId.of(zoneId))
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    return zonedDateTime.format(formatter)
}
fun getTimeByZone(zoneId: String): String {
    val zonedDateTime = ZonedDateTime.now(ZoneId.of(zoneId))
    val formatter = DateTimeFormatter.ofPattern("hh:mm a")
    return zonedDateTime.format(formatter)
}

fun getz(zoneId: String): String {
    val zonedDateTime = ZonedDateTime.now(ZoneId.of(zoneId))
    val formatter = DateTimeFormatter.ofPattern("z")
    return zonedDateTime.format(formatter)
}
