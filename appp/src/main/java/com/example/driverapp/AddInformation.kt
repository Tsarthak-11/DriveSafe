package com.example.driverapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.driverapp.DriverInformation
import com.sarthak.driverlogin.DashboardActivity

class AddInformation : AppCompatActivity() {
    var driversname: EditText? = null
    var vehicleno: EditText? = null
    var travellingfrom: EditText? = null
    var travellingto: EditText? = null
    var databaseReference: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_information)
        driversname = findViewById<View>(R.id.name) as EditText
        vehicleno = findViewById<View>(R.id.vehicleno) as EditText
        travellingfrom = findViewById<View>(R.id.travellingfrom) as EditText
        travellingto = findViewById<View>(R.id.travellingto) as EditText
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    fun onClickBackToHome(view: View?) {
        val intent=Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    fun onClickUploadInformation(view: View?) {
        val dn: String = driversname!!.text.toString()
        val vn: String = vehicleno!!.text.toString()
        val tf: String = travellingfrom!!.text.toString()
        val tt: String = travellingto!!.text.toString()
        if (!(TextUtils.isEmpty(dn) || TextUtils.isEmpty(vn) || TextUtils.isEmpty(tf) || TextUtils.isEmpty(
                tt
            ))
        ) {
            databaseReference!!.push()
            val driverInformation = DriverInformation(dn, vn, tf, tt, 0.0, 0.0, false)
            databaseReference!!.setValue(driverInformation)
            Toast.makeText(this, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Input Field Cannot be Blank!!!", Toast.LENGTH_SHORT).show()
        }
    }
}