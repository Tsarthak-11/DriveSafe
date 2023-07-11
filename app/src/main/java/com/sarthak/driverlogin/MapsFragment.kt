package com.sarthak.driverlogin

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.sarthak.driverlogin.MapsFragment
import kotlinx.android.synthetic.main.fragment_maps.*

class MapsFragment : Fragment() {

    lateinit var startLocation : Button
    lateinit var stopLocation : Button
    lateinit var nearbyHospital : Button
    lateinit var nearbyPoliceStation : Button

    private var broadcastReceiver: BroadcastReceiver? = null
    var latitude: String? = null
    var longitude: String? = null

    override fun onResume() {
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
        requireActivity().registerReceiver(broadcastReceiver, IntentFilter("location_update"))
    }
    override fun onDestroy() {
        super.onDestroy()
        if (broadcastReceiver != null) {
            requireActivity().unregisterReceiver(broadcastReceiver)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_maps, container, false)


        startLocation = view.findViewById(R.id.btn_start)
        stopLocation= view.findViewById(R.id.btn_stop)
        nearbyHospital = view.findViewById(R.id.nearbybtn)
        nearbyPoliceStation= view.findViewById(R.id.nearbyps)

        nearbyHospital.setOnClickListener {

                val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=hospitals")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
        }

        nearbyPoliceStation.setOnClickListener{
            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude?q=police stations")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }


        if (!runtime_permissions()) {
            enable_buttons()
        }

    return view
    }

    fun enable_buttons() {
        startLocation.setOnClickListener {
            val i = Intent(activity?.applicationContext, GPS_service::class.java)
            context?.startService(i)
        }

        stopLocation.setOnClickListener {

            stopLocation.setOnClickListener {
                val i = Intent(activity?.applicationContext, GPS_service::class.java)
                context?.stopService(i)

            }

        }
    }

    private fun runtime_permissions(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), 101
            )
            return true
        }
        return false
    }

    @Deprecated("Deprecated in Java")
    override fun  onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String?>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons()
            } else {
                runtime_permissions()
            }
        }
    }



}