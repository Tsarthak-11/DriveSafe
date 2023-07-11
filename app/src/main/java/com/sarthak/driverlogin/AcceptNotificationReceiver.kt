package com.sarthak.driverlogin

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*

class AcceptNotificationReceiver : BroadcastReceiver() {

    // Create an instance of the Firebase database
    private val database = FirebaseDatabase.getInstance()

    // Get a reference to the root node of the database
    private val databaseReference = database.reference

    companion object {
        const val ACTION_ACCEPT = "ACTION_ACCEPT"
    }

    override fun onReceive(context: Context, intent: Intent) {

        val mediaPlayer = MediaPlayer.create(context, R.raw.ambulance_siren)
        mediaPlayer.stop()
        mediaPlayer.release()

        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("accident_flag")
        myRef.setValue(true).addOnSuccessListener {
            Toast.makeText(context, "Alert", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "" + it.message, Toast.LENGTH_SHORT).show()
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(1)

        val latitudeRef = databaseReference.child("latitude")
        val longitudeRef = databaseReference.child("longitude")

        latitudeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val latitudeString = dataSnapshot.value?.toString()

                // Check if latitude value is not null before parsing it as a double
                if (latitudeString != null) {
                    val latitude = latitudeString.toDouble()

                    longitudeRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val longitudeString = dataSnapshot.value?.toString()

                            // Check if longitude value is not null before parsing it as a double
                            if (longitudeString != null) {
                                val longitude = longitudeString.toDouble()

                                // Get the emergency contacts from the database
                                val emergencyContactsRef = databaseReference.child("Emergency_Contacts")
                                emergencyContactsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Get the contact details from the dataSnapshot
                                        val name1 = dataSnapshot.child("name1").value.toString()
                                        val number1 = dataSnapshot.child("number1").value.toString()
                                        val name2 = dataSnapshot.child("name2").value.toString()
                                        val number2 = dataSnapshot.child("number2").value.toString()
                                        val name3 = dataSnapshot.child("name3").value.toString()
                                        val number3 = dataSnapshot.child("number3").value.toString()

                                        // Get the current location
                                        val location = "http://maps.google.com/maps?q=$latitude,$longitude"

                                        // Send SMS to the emergency contacts
                                        val smsManager = SmsManager.getDefault()
                                        smsManager.sendTextMessage(number1, null, "Sos!!! Emergency \nI need Help \nLocation: $location", null, null)
                                        smsManager.sendTextMessage(number2, null, "Sos!!! Emergency \nI need Help \nLocation:  $location", null, null)
                                        smsManager.sendTextMessage(number3, null, "Sos!!! Emergency \nI need Help \nLocation:  $location", null, null)
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        // Handle database error
                                    }
                                })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle database error
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
