package com.example.driverapp

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.provider.Settings
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class GPS_Service : Service() {
    private var listener: LocationListener? = null
    private var locationManager: LocationManager? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val database = FirebaseDatabase.getInstance()
                var myRef = database.getReference("latitude")
                myRef.setValue(location.latitude)
                myRef = database.getReference("longitude")
                myRef.setValue(location.longitude)
                val i = Intent("location_update")
                i.putExtra("latitude", location.latitude)
                i.putExtra("longitude", location.longitude)
                sendBroadcast(i)
            }

            override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
            override fun onProviderEnabled(s: String) {}
            override fun onProviderDisabled(s: String) {
                val i = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(i)
            }
        }
        locationManager = applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0f,
            listener as LocationListener
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null) {
            locationManager!!.removeUpdates(listener!!)
        }
    }
}