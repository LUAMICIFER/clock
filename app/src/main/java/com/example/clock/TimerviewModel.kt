package com.example.clock

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalTime

class TimerviewModel :ViewModel(){
    private var _selectedtime = MutableLiveData<LocalTime>(LocalTime.MIDNIGHT)
    val selectedtime :LiveData<LocalTime> = _selectedtime
    fun _setselectedtime(newtime:LocalTime){
        TimerManager.updateSelectedTime(newtime)
        _getselectedtime()
    }
    fun _getselectedtime(){
        _selectedtime.value=TimerManager.selectedTime
    }



    private var _isTimerRunning = MutableLiveData<Boolean>(false)
    val istimerrunning :LiveData<Boolean> = _isTimerRunning
    fun _setisTimerRunning(istimerrunning:Boolean){
        TimerManager.updateTimerRunning(istimerrunning)
        _getisTimerRunning()
    }
    fun _getisTimerRunning(){
        _isTimerRunning.value=TimerManager.isTimerRunning
    }


    private var _progress = MutableLiveData<Float>(0f)
    val progress :LiveData<Float> = _progress
    fun _setprogress(progress:Float){
        TimerManager.updateProgress(progress)
        _getprogress()
    }
    fun _getprogress(){
        _progress.value=TimerManager.progress
    }


    private var _playPause = MutableLiveData<Boolean>(true)
    var playPause :LiveData<Boolean> = _playPause
    fun _setPlayPause(newState :Boolean){
        TimerManager.updatePlayPause(newState)
        _getPlayPause()
    }
    fun _getPlayPause(){
        _playPause.value=TimerManager.playPause
    }

}