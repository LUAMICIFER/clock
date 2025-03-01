package com.example.clock

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun Navi(navController :NavHostController){
//    val navController : NavHostController
//    navController = rememberNavController()
    NavHost(navController= navController , startDestination = "Alarm"){
        composable("Alarm"){ Alar(Alarm(navController.context)) }
        composable("Stopwatch"){ Stopwatch() }
        composable("WorldClock"){ WorldClock() }
        composable("Timer"){ Timer() }

    }
}