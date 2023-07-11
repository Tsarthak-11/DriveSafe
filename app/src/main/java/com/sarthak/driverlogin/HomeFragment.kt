package com.sarthak.driverlogin

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class HomeFragment : Fragment() {

    lateinit var iAmSafe: Button
    lateinit var iNeedHelp: Button

    private lateinit var mediaPlayer: MediaPlayer
    private var timer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val notificationManager = NotificationManagerCompat.from(requireContext())
        val channelId = "channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = " DriveSafe"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannelCompat.Builder(channelId, importance)
                .setName(channelName)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM), AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build())
                .build()
            notificationManager.createNotificationChannel(channel)
        }




        iAmSafe = view.findViewById(R.id.safebtn)
        iNeedHelp = view.findViewById(R.id.helpbtn)

        iAmSafe.setOnClickListener {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val myRef: DatabaseReference = database.getReference("accident_flag")
            myRef.setValue(false)
        }

        iNeedHelp.setOnClickListener {



// Set up the accept intent
            val acceptIntent = Intent(context, AcceptNotificationReceiver::class.java).apply {
                action = AcceptNotificationReceiver.ACTION_ACCEPT
                putExtra("notificationId", 1)
            }
            val acceptPendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                acceptIntent,
                PendingIntent.FLAG_IMMUTABLE
            )

            // Create an intent for the DenyNotificationReceiver
            val denyIntent = Intent(context, DenyNotificationReceiver::class.java).apply {
                action = DenyNotificationReceiver.ACTION_DENY
                putExtra("notificationId", 1)
                putExtra("timerId", System.identityHashCode(timer))


            }
            val denyPendingIntent = PendingIntent.getBroadcast(
                requireContext(),
                0,
                denyIntent,
                PendingIntent.FLAG_IMMUTABLE
            )


// Set up the notification builder
            val soundUri = Uri.parse("android.resource://" + requireContext().packageName + "/" + R.raw.ambulance_siren)

            val mediaPlayer = MediaPlayer.create(requireContext(), soundUri)
            mediaPlayer.start()

            val notificationBuilder = NotificationCompat.Builder(requireContext(), "channel_id")
                .setSmallIcon(R.drawable.appicon)
                .setContentTitle("Accident Detected")
                .setContentText("Have you met with an accident?")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(null)
                .addAction(R.drawable.ic_check, "Accept", acceptPendingIntent)
                .addAction(R.drawable.ic_close, "Deny", denyPendingIntent)


// Show the notification
            val notificationId = 1
            val notificationManager = NotificationManagerCompat.from(requireContext())
            //    notificationManager.createNotificationChannel("channel_id","Channel name",NotificationManager.IMPORTANCE_HIGH)
            notificationManager.notify(notificationId, notificationBuilder.build())

        //    val intent = Intent(requireContext(), NotificationSoundService::class.java)
          //  intent.action = NotificationSoundService.ACTION_START
           // ContextCompat.startForegroundService(requireContext(), intent)


            timer = Timer()
            timer?.schedule(object : TimerTask() {
                override fun run() {
                    acceptPendingIntent.send()
                }
            }, 30000)
        }


        return view
    }


    fun cancelTimer(timerId: Int) {
        if (System.identityHashCode(timer) == timerId) {
            timer?.cancel()
            mediaPlayer.stop()
        }


    }
}