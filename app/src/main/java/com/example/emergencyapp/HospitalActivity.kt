package com.example.emergencyapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.emergencyapp.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HospitalActivity : AppCompatActivity() {

    lateinit var HospitalName: EditText
    lateinit var PatientRegistrationDate: EditText
    lateinit var PatientRegistrationTime: EditText
    lateinit var PatientHealthStatus: EditText
    lateinit var updatehospitalbtn : Button
    var databaseReference: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hospital)

        HospitalName = findViewById(R.id.et_hospital_name)
        PatientRegistrationDate = findViewById(R.id.et_date)
        PatientRegistrationTime =findViewById(R.id.et_time)
        PatientHealthStatus =findViewById(R.id.et_comment)
        updatehospitalbtn = findViewById(R.id.btn_upd)
        databaseReference = FirebaseDatabase.getInstance().reference

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        HospitalName.setText(sharedPreferences.getString("HospitalName", ""))
        PatientRegistrationDate.setText(sharedPreferences.getString("PatientRegistrationDate", ""))
        PatientRegistrationTime.setText(sharedPreferences.getString("PatientRegistrationTime", ""))
        PatientHealthStatus.setText(sharedPreferences.getString("PatientHealthStatus", ""))




        databaseReference = FirebaseDatabase.getInstance().getReference().child("HospitalInformation")


        updatehospitalbtn.setOnClickListener {

            val hospitalnam : String = HospitalName!!.text.toString().trim()
            val patRegDate : String = PatientRegistrationDate!!.text.toString().trim()
            val patRegTime : String = PatientRegistrationTime!!.text.toString().trim()
            val patHealthStat : String = PatientHealthStatus!!.text.toString().trim()


            if (!(TextUtils.isEmpty(hospitalnam) || TextUtils.isEmpty(patRegDate) || TextUtils.isEmpty(patRegTime) || TextUtils.isEmpty(patHealthStat))
            ) {
                databaseReference!!.push()
                val hospitalInfo = HospitalInformation(hospitalnam,patRegDate,patRegTime,patHealthStat)
                databaseReference!!.setValue(hospitalInfo)
                Toast.makeText(this, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Input Field Cannot be Blank!!!", Toast.LENGTH_SHORT).show()
            }

            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("HospitalName", HospitalName.text.toString())
            editor.putString("PatientRegistrationDate", PatientRegistrationDate.text.toString())
            editor.putString("PatientRegistrationTime", PatientRegistrationTime.text.toString())
            editor.putString("PatientHealthStatus", PatientHealthStatus.text.toString())

            editor.apply()

        }



    }


}