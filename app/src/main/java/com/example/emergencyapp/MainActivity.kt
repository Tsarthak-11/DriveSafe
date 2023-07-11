package com.example.emergencyapp

import android.app.*
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import android.widget.TextView
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import android.widget.Toast
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import java.lang.Boolean

class MainActivity : AppCompatActivity() {


    var databaseReference: DatabaseReference? = null
    var coordinates: TextView? = null
    var messagetext: TextView? = null
    var latitude: String? = null
    var longitude: String? = null

    lateinit var AmbulanceDriverName: EditText
    lateinit var AmbulanceDriverContact: EditText
    lateinit var AmbulanceVehicleNumber: EditText
    lateinit var updatebtn : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        databaseReference = FirebaseDatabase.getInstance().reference
        coordinates = findViewById<View>(R.id.coordinates) as TextView
        messagetext = findViewById<View>(R.id.messagetext) as TextView
        updatebtn = findViewById(R.id.updatebtn)

        AmbulanceDriverName = findViewById(R.id.driver_name)
        AmbulanceDriverContact = findViewById(R.id.driver_contact)
        AmbulanceVehicleNumber =findViewById(R.id.vehicle_number)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        AmbulanceDriverName.setText(sharedPreferences.getString("driverName", ""))
        AmbulanceDriverContact.setText(sharedPreferences.getString("driverContact", ""))
        AmbulanceVehicleNumber.setText(sharedPreferences.getString("vehicleNumber", ""))




        databaseReference!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (Boolean.parseBoolean(dataSnapshot.child("accident_flag").value.toString()) == false) {
                    messagetext!!.setTextColor(resources.getColor(R.color.green))
                    messagetext!!.text = """STATUS :- 
${dataSnapshot.child("driversname").value.toString()} is Fine."""
                 //   coordinates!!.text = "" // Clear the coordinates TextView

                } else {
                    messagetext!!.setTextColor(resources.getColor(R.color.red))
                    messagetext!!.text = """STATUS 
${dataSnapshot.child("driversname").value.toString()} met with an Accident."""
                    promptnotification()
                    longitude = dataSnapshot.child("longitude").value.toString()
                    latitude = dataSnapshot.child("latitude").value.toString()
                    coordinates!!.text = "Coordinates :- \n $latitude  $longitude"

                }



            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to Get Information!!!", Toast.LENGTH_SHORT).show()
            }
        })


        databaseReference = FirebaseDatabase.getInstance().getReference().child("AmbulanceDriverInformation")


            updatebtn.setOnClickListener {

                val drivnam : String = AmbulanceDriverName!!.text.toString().trim()
                val drivcon : String = AmbulanceDriverContact!!.text.toString().trim()
                val vehicalnum : String = AmbulanceVehicleNumber!!.text.toString().trim()

                if (!(TextUtils.isEmpty(drivnam) || TextUtils.isEmpty(drivcon) || TextUtils.isEmpty(vehicalnum))
                ) {
                    databaseReference!!.push()
                    val ambulanceInfo = AmbulanceInformation(drivnam,drivcon,vehicalnum)
                    databaseReference!!.setValue(ambulanceInfo)
                    Toast.makeText(this, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Input Field Cannot be Blank!!!", Toast.LENGTH_SHORT).show()
                }

                val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("driverName", AmbulanceDriverName.text.toString())
                editor.putString("driverContact", AmbulanceDriverContact.text.toString())
                editor.putString("vehicleNumber", AmbulanceVehicleNumber.text.toString())
                editor.apply()

            }



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
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(this@MainActivity, 0, intent, PendingIntent.FLAG_IMMUTABLE)
            val notification = Notification.Builder(this@MainActivity)
                    .setContentTitle("ACCIDENT ALERT")
                    .setContentText("Driver meet with an Accident!!!")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setChannelId(channelId)
                    .setContentIntent(pendingIntent)
            notificationManager.notify(notifyId, notification.build())
        }
    }

    fun onClickGetAddress(view: View?) {
        val addressDialog = longitude?.let { latitude?.let { it1 -> AddressDialog(it, it1) } }
        if (addressDialog != null) {
            addressDialog.show(supportFragmentManager, "example dialog")
        }
    }

    fun onClickLocateOnMap(view: View?) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun onClickNearby(view: View?) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=hospitals")
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

    fun onClickNearbyps(view: View?) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=police stations")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}