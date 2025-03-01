package com.example.clock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.SystemClock
import androidx.compose.runtime.remember
import java.util.Calendar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Alarm(private val context: Context) {
    private val gson = Gson()
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//    private val alarms = mutableListOf<AlarmItem>()
private val sharedPrefs: SharedPreferences = context.getSharedPreferences("alarms", Context.MODE_PRIVATE)

    fun scheduleAlarm(hour :Int ,minute :Int,requestCode:Int){
        val calendar =Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)
        calendar.set(Calendar.SECOND,0)
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,requestCode, Intent(context,AlarmReceiver::class.java),PendingIntent.FLAG_IMMUTABLE
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
        val alarms = getAlarms().toMutableList()
        alarms.add(AlarmItem(hour, minute, requestCode))
        saveAlarms(alarms)
    }
    fun cancelAlarm(requestCode: Int){
        val pendingIntent = PendingIntent.getBroadcast(
            context,requestCode, Intent(context,AlarmReceiver::class.java),PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        val alarms = getAlarms().filter { it.requestCode != requestCode }
        saveAlarms(alarms)
    }
    fun getAlarms(): List<AlarmItem> {
        val alarmsJson = sharedPrefs.getString("alarms_list", "[]")
        val type = object : TypeToken<List<AlarmItem>>() {}.type
        return gson.fromJson<List<AlarmItem>>(alarmsJson, type) ?: emptyList()
    }

    private fun saveAlarms(alarms: List<AlarmItem>) {
        sharedPrefs.edit().putString("alarms_list", gson.toJson(alarms)).apply()
    }

}

data class AlarmItem(val hour: Int, val minute: Int, val requestCode: Int)