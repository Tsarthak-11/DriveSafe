package com.example.emergencyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.example.emergencyapp.R

class MainActivity0 : AppCompatActivity() {

    lateinit var policelayout : RelativeLayout
    lateinit var ambulancelayout : RelativeLayout
    lateinit var hospitallayout : RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main0)

        policelayout = findViewById(R.id.POlICE)
        ambulancelayout= findViewById(R.id.AMBULANCE)
        hospitallayout= findViewById(R.id.HOSPITAL)
        policelayout.setOnClickListener {

            //val intent = Intent(this,PoliceActivity::class.java)
           // startActivity(intent)
        }

        ambulancelayout.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        hospitallayout.setOnClickListener {
            val intent = Intent(this,HospitalActivity::class.java)
            startActivity(intent)
        }

        policelayout.setOnClickListener {
            val intent = Intent(this,PoliceActivity::class.java)
            startActivity(intent)
        }



    }
}