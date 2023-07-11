package com.example.emergencyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.emergencyapp.R
import com.google.firebase.database.*

class ViewUserInfoActivity : AppCompatActivity() {

    var driversname: TextView? = null
    var vehicleno: TextView? = null
    var travellingfrom: TextView? = null
    var travellingto: TextView? = null
    var emergencyContacts : TextView? = null

    var ambulancedriversname: TextView? = null
    var ambulancevehicleno: TextView? = null
    var ambulancecontactnno: TextView? = null

    var hospitalname: TextView? = null
    var patientregisterdate: TextView? = null
    var patientregistertime: TextView? = null
    var patienthealthstatus: TextView? = null



    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_user_info)
        driversname = findViewById<View>(R.id.driversname) as TextView
        vehicleno = findViewById<View>(R.id.vehicleno) as TextView
        travellingfrom = findViewById<View>(R.id.travellingfrom) as TextView
        travellingto = findViewById<View>(R.id.travellingto) as TextView
        emergencyContacts = findViewById<View>(R.id.emergency_contact) as TextView

        ambulancedriversname = findViewById<View>(R.id.ambulancedriversname) as TextView
        ambulancevehicleno = findViewById<View>(R.id.ambulancenumber) as TextView
        ambulancecontactnno = findViewById<View>(R.id.ambulancecontactno) as TextView

        hospitalname = findViewById<View>(R.id.hospitalname) as TextView
        patientregisterdate = findViewById<View>(R.id.patientregdate) as TextView
        patientregistertime = findViewById<View>(R.id.patientregtime) as TextView
        patienthealthstatus = findViewById<View>(R.id.patienthealthstatus) as TextView


        databaseReference = FirebaseDatabase.getInstance().reference
        databaseReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                driversname!!.text =
                    "Driver Name :- " + dataSnapshot.child("driversname").value.toString()
                vehicleno!!.text =
                    "Vehicle No :- " + dataSnapshot.child("vehicleno").value.toString()
                travellingto!!.text =
                    "Travelling To :- " + dataSnapshot.child("travellingto").value.toString()
                travellingfrom!!.text =
                    "Travelling From :- " + dataSnapshot.child("travellingfrom").value.toString()

                val contactsMap = dataSnapshot.child("Emergency_Contacts").value as HashMap<*, *>
                val contactsList = ArrayList<String>()

// Loop through the contacts and concatenate the name and number for each contact
                for (i in 1..3) {
                    val name = contactsMap["name$i"] as String
                    val number = contactsMap["number$i"] as String
                    val contact = "$name: $number"
                    contactsList.add(contact)
                }

// Join the contacts list into a single string with a new line separator
                val contactsString = contactsList.joinToString("\n")

// Display the contacts in the text view
                emergencyContacts!!.text = "Emergency Contacts:\n\n$contactsString"

                // Assign values for ambulance driver information
                ambulancedriversname!!.text = "Ambulance Driver Name: " + dataSnapshot.child("AmbulanceDriverInformation").child("ambulancedriverName").value.toString()
                ambulancevehicleno!!.text = "Ambulance Vehicle No: " + dataSnapshot.child("AmbulanceDriverInformation").child("ambulancevehicleNumber").value.toString()
                ambulancecontactnno!!.text = "Driver Contact No: " + dataSnapshot.child("AmbulanceDriverInformation").child("ambulancedriverContact").value.toString()

                // Assign values for hospital information
                hospitalname!!.text = "Hospital Name: " + dataSnapshot.child("HospitalInformation").child("hospitalName").value.toString()
                patientregisterdate!!.text = "Register Date: " + dataSnapshot.child("HospitalInformation").child("patientRegistrationDate").value.toString()
                patientregistertime!!.text = "Time: " + dataSnapshot.child("HospitalInformation").child("patientRegistrationTime").value.toString()
                patienthealthstatus!!.text = "Health Status: " + dataSnapshot.child("HospitalInformation").child("patientHealthStatus").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ViewUserInfoActivity,
                    "Failed to Fetch Information!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}