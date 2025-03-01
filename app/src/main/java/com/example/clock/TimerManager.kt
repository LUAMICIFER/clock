package com.example.clock

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalTime

object TimerManager {
    var selectedTime by mutableStateOf(LocalTime.MIDNIGHT)
    private set
    fun getteSelectedTime(): LocalTime {
        return selectedTime
    }
    fun updateSelectedTime(newTime:LocalTime){
        selectedTime= newTime
    }


    var isTimerRunning by mutableStateOf(false)
        private set
    fun getteTimerRunning():Boolean{
        return isTimerRunning
    }
    fun updateTimerRunning(newstate:Boolean){
        isTimerRunning=newstate
    }


    var progress by mutableStateOf(0f)
        private set
    fun getteProgress():Float{
        return progress
    }
    fun updateProgress(newprogress:Float){ //progress wale ko call karke +1f wahi pr se bhej denge
        progress=newprogress
    }

    var playPause by mutableStateOf(true)
        private set
    fun updatePlayPause(newState:Boolean){
        playPause=newState
    }
    fun gettePlayPause():Boolean{
        return playPause
    }
    private var mediaPlayer: MediaPlayer? = null

//    fun playSound(context: Context) {
//        mediaPlayer?.release() // Release old instance
//        mediaPlayer = MediaPlayer.create(context, R.raw.alarm_sound)
//        mediaPlayer?.start()
//    }

}