package com.example.emergencyapp

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.emergencyapp.R
import com.google.firebase.database.*
import java.lang.Boolean

class PoliceActivity : AppCompatActivity() {

    var databaseReference: DatabaseReference? = null
    var coordinates: TextView? = null
    var messagetext: TextView? = null
    var latitude: String? = null
    var longitude: String? = null
    lateinit var viewUserInfo : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police)

        databaseReference = FirebaseDatabase.getInstance().reference
        coordinates = findViewById<View>(R.id.coordinates1) as TextView
        messagetext = findViewById<View>(R.id.messagetext1) as TextView
        viewUserInfo = findViewById(R.id.btn_upd)

        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (Boolean.parseBoolean(dataSnapshot.child("accident_flag").value.toString()) == false) {
                    messagetext!!.setTextColor(resources.getColor(R.color.green))
                    messagetext!!.text = """STATUS :- 
${dataSnapshot.child("driversname").value.toString()} is Fine."""
                } else {
                    messagetext!!.setTextColor(resources.getColor(R.color.red))
                    messagetext!!.text = """STATUS 
${dataSnapshot.child("driversname").value.toString()} met with an Accident."""

                    longitude = dataSnapshot.child("longitude").value.toString()
                    latitude = dataSnapshot.child("latitude").value.toString()
                    coordinates!!.text = "Coordinates :- \n $latitude  $longitude"
                }


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PoliceActivity, "Failed to Get Information!!!", Toast.LENGTH_SHORT).show()
            }
        })

        viewUserInfo.setOnClickListener {

            val intent = Intent(this,ViewUserInfoActivity::class.java)
            startActivity(intent)

        }

    }

    fun onClickLocateOnMap(view: View?) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun onClickNavigation(view: View?) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun onClickGetAddress(view: View?) {
        val addressDialog = longitude?.let { latitude?.let { it1 -> AddressDialog(it, it1) } }
        if (addressDialog != null) {
            addressDialog.show(supportFragmentManager, "example dialog")
        }
        fun promptnotification() {
            if (Build.VERSION.SDK_INT >= 26) {
                val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                val channelId = "some_channel_id"
                val channelName: CharSequence = "Some Channel"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val notificationChannel = NotificationChannel(channelId, channelName, importance)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 100)
                notificationChannel.setShowBadge(true)
                notificationChannel.lockscreenVisibility = 1
                notificationManager.createNotificationChannel(notificationChannel)
                val groupId = "some_group_id"
                val groupName: CharSequence = "Some Group"
                //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannelGroup(NotificationChannelGroup(groupId, groupName))

                //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                val notifyId = 1
                //String channelId = "some_channel_id";
                val intent = Intent(this@PoliceActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                val pendingIntent = PendingIntent.getActivity(this@PoliceActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                val notification = Notification.Builder(this@PoliceActivity)
                    .setContentTitle("ACCIDENT ALERT")
                    .setContentText("Driver meet with an Accident!!!")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
                notificationManager.notify(notifyId, notification.build())
            }
        }

    }
}