package com.example.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import androidx.compose.runtime.remember
import java.util.Calendar

class Alarm(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val alarms = mutableListOf<AlarmItem>()
    fun scheduleAlarm(hour :Int ,minute :Int,requestcode:Int){
        val calendar =Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        calendar.set(Calendar.SECOND,0)
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,requestcode, Intent(context,AlarmReceiver::class.java),PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, //for exact time //use elapsed_realtime_wakeup can be used for the for say you want to trigeer the alaram for 20 minutes after the user wakes up after every 20 minutes
            calendar.timeInMillis,
            pendingIntent
            )
//        alarmManager.setRepeating(
//            AlarmManager.RTC_WAKEUP,
//            SystemClock.elapsedRealtime()+ fiveseconds,
//            1000*60*5,
//            pendingIntent
//        )
        alarms.add(AlarmItem(hour, minute, requestcode))
    }
    fun cancelAlarm(){
        val pendingIntent = PendingIntent.getBroadcast(
            context,1, Intent(context,AlarmReceiver::class.java),PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
    fun getAlarms(): List<AlarmItem> {
        return alarms
    }
}

data class AlarmItem(val hour: Int, val minute: Int, val requestCode: Int)