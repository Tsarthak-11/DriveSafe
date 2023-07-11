package com.sarthak.driverlogin

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat

class DenyNotificationReceiver : BroadcastReceiver() {
    companion object {
        const val ACTION_DENY = "ACTION_DENY"
    }

    override fun onReceive(context: Context, intent: Intent) {



        val mediaPlayer = MediaPlayer.create(context, R.raw.ambulance_siren)
        mediaPlayer.stop()
        mediaPlayer.release()

        val notificationId = intent.getIntExtra("notificationId", 1)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)


        val timerId = intent.getIntExtra("timerId", 0)
       // val activity = context as DashboardActivity
        val activity = context as DashboardActivity
        val fragmentManager = activity.supportFragmentManager
        val fragment = fragmentManager.findFragmentByTag("HomeFragment") as? HomeFragment
        fragment?.cancelTimer(timerId)

        Toast.makeText(context, "You denied the notification.", Toast.LENGTH_SHORT).show()

        val stopIntent = Intent(context, NotificationSoundService::class.java)
        context.stopService(stopIntent)


    }
}
