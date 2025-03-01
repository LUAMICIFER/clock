package com.example.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.clock.ui.theme.ClockTheme
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClockTheme {
                MainScreen()
            }
        }
    }
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(
        "Alarm" to R.drawable.baseline_alarm_24,
        "Stopwatch" to R.drawable.baseline_timer_24,
        "WorldClock" to R.drawable.baseline_timer_24,
        "Timer" to R.drawable.baseline_hourglass_bottom_24
    )
    var selectedIndex by rememberSaveable { mutableStateOf(0) }
//    val pagerState = rememberPagerState(initialPage = selectedIndex)
    Scaffold(
        bottomBar = {
            AnimatedNavigationBar(
                selectedIndex = selectedIndex,
                barColor = Color(0xFF6200EE),
                cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                ballColor = MaterialTheme.colorScheme.primary
            ) {
                items.forEachIndexed { index, (title, iconRes) ->
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .noRippleClickable { selectedIndex = index },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = title,
                            modifier = Modifier.size(30.dp),
                            tint = if (selectedIndex == index) Color.White else Color.Gray
                        )
                            if (selectedIndex == index) {
                                Text(
                                    text = title,
                                    color = Color.White,
                                    fontSize = (10.sp)
                                )
                            }
                        }

                    }
                }
            }
        }
    ) { paddingValues ->
        // You need to implement screen navigation
        Box(modifier = Modifier.padding(paddingValues)) {
            LaunchedEffect(selectedIndex) {
                val route = when (selectedIndex) {
                    0 -> "Alarm"
                    1 -> "Stopwatch"
                    2 -> "WorldClock"
                    3 -> "Timer"
                    else -> "Alarm"
                }
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = false
                    } // Avoid stacking
                    launchSingleTop = true // Prevent duplicate instances
                }
            }

            Navi(navController = navController)
        }
    }
}
