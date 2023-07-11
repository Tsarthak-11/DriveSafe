package com.sarthak.driverlogin


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
//import com.example.driverapp.AddInfo

class ViewInfoActivity : AppCompatActivity() {
    var driversname: TextView? = null
    var vehicleno: TextView? = null
    var travellingfrom: TextView? = null
    var travellingto: TextView? = null
    var emergencyContacts : TextView? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_info)
        driversname = findViewById<View>(R.id.driversname) as TextView
        vehicleno = findViewById<View>(R.id.vehicleno) as TextView
        travellingfrom = findViewById<View>(R.id.travellingfrom) as TextView
        travellingto = findViewById<View>(R.id.travellingto) as TextView
        emergencyContacts = findViewById<View>(R.id.emergency_contact) as TextView
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


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@ViewInfoActivity,
                    "Failed to Fetch Information!!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun onClickBackToHome(view: View?) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun onClickUpdateInformation(view: View?) {
        val intent = Intent(this, AddInfoActivity::class.java)
        startActivity(intent)
    }
}