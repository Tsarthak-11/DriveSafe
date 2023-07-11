package com.sarthak.driverlogin
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class NotificationSoundService : Service() {

    companion object {
        const val ACTION_START = "ACTION_START"
    }
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.ambulance_siren)
        mediaPlayer?.isLooping = true
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer?.start()
        return START_NOT_STICKY
    }



    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        mediaPlayer?.release()
    }
}
