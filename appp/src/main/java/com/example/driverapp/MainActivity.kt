package com.example.driverapp

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    var addinformation: Button? = null
    var btn_start: Button? = null
    var btn_stop: Button? = null
    var btn_locate: Button? = null
    var textView: TextView? = null
    private var broadcastReceiver: BroadcastReceiver? = null
    var latitude: String? = null
    var longitude: String? = null
    protected override fun onResume() {
        super.onResume()
        if (broadcastReceiver == null) {
            broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    latitude = intent.extras!!["latitude"].toString()
                    longitude = intent.extras!!["longitude"].toString()
                    textView!!.text = """Coordinates :- 
${intent.extras!!["latitude"].toString()} , ${intent.extras!!["longitude"].toString()}"""
                }
            }
        }
        registerReceiver(broadcastReceiver, IntentFilter("location_update"))
    }

    protected override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
    }

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addinformation = findViewById(R.id.addinformation) as Button?
        btn_locate = findViewById(R.id.locatebtn) as Button?
        btn_start = findViewById(R.id.btn_start) as Button?
        btn_stop = findViewById(R.id.btn_stop) as Button?
        textView = findViewById(R.id.textView) as TextView?
        if (!runtime_permissions()) {
            enable_buttons()
        }
    }

    private fun enable_buttons() {
        btn_start!!.setOnClickListener {
            val i = Intent(getApplicationContext(), GPS_Service::class.java)
            startService(i)
        }
        btn_stop!!.setOnClickListener {
            val i = Intent(getApplicationContext(), GPS_Service::class.java)
            stopService(i)
        }
    }

    private fun runtime_permissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 100
            )
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons()
            } else {
                runtime_permissions()
            }
        }
    }

    fun onClickAddInformation(view: View?) {
        val intent: Intent = Intent(this, AddInformation::class.java)
        startActivity(intent)
    }

    fun onClickGetInformation(view: View?) {
        val intent: Intent = Intent(this, ViewInformation::class.java)
        startActivity(intent)
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

    fun onClickNearbyps(view: View?) {
        val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=police stations")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    fun OnClickAccident(view: View?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("accident_flag")
        myRef.setValue("true").addOnSuccessListener(object : OnSuccessListener<Void?> {
            override fun onSuccess(unused: Void?) {
                Toast.makeText(this@MainActivity, "Alert", Toast.LENGTH_SHORT).show()
            }
        })
            .addOnFailureListener(object : OnFailureListener {
                override fun onFailure(@NonNull e: Exception) {
                    Toast.makeText(this@MainActivity, "" + e.message, Toast.LENGTH_SHORT).show()
                }
            })
    }

    fun OnClickIAmSafe(view: View?) {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef: DatabaseReference = database.getReference("accident_flag")
        myRef.setValue(false)
    }
}