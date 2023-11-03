package com.example.mad_practical_8_22172012032
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextClock
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addAlarm : MaterialButton = findViewById(R.id.create)
        val card : MaterialCardView = findViewById(R.id.card2)

        card.visibility = View.GONE

        addAlarm.setOnClickListener {
            TimePickerDialog(this, {tp, hour, minute -> setAlarmTime(hour, minute) }, Calendar.getInstance().get(Calendar.HOUR), Calendar.getInstance().get(Calendar.MINUTE), false).show()
            card.visibility = View.VISIBLE
        }

        val cancelAlarm : MaterialButton = findViewById(R.id.cancel)
        cancelAlarm.setOnClickListener {
            stop()
            card.visibility = View.GONE
        }
    }

    fun setAlarmTime(hour : Int, minute : Int){
        val alarmtime = Calendar.getInstance()
        val year = alarmtime.get(Calendar.YEAR)
        val month = alarmtime.get(Calendar.MONTH)
        val date = alarmtime.get(Calendar.DATE)
        alarmtime.set(year, month, date, hour, minute, 0)
        val textAlarmTime : TextClock = findViewById(R.id.clocktime2)
        textAlarmTime.text = SimpleDateFormat("hh:mm:ss a").format(alarmtime.time)
        setAlarm(alarmtime.timeInMillis, AlarmBroadcastReceiver.ALARMSTART)
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show()
    }

    fun stop() {
        setAlarm(-1, AlarmBroadcastReceiver.ALARMSTOP)
    }

    @SuppressLint("ScheduleExactAlarm")
    fun setAlarm(militime : Long, action : String) {
        val intentalarm = Intent(applicationContext, AlarmBroadcastReceiver::class.java)
        intentalarm.putExtra(AlarmBroadcastReceiver.ALARMKEY, action)
        val pendingintent = PendingIntent.getBroadcast(applicationContext, 4356, intentalarm, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmmanager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (action == AlarmBroadcastReceiver.ALARMSTART) {
            alarmmanager.setExact(AlarmManager.RTC_WAKEUP, militime, pendingintent)
        }
        else if (action == AlarmBroadcastReceiver.ALARMSTOP) {
            alarmmanager.cancel(pendingintent)
            sendBroadcast(intentalarm)
        }
    }
}