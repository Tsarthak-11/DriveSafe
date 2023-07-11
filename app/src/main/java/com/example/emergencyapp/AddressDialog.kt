package com.example.emergencyapp

import android.app.AlertDialog
import android.app.Dialog
import android.location.Address
import androidx.appcompat.app.AppCompatDialogFragment
import android.os.Bundle
import android.location.Geocoder
import com.example.emergencyapp.AddressDialog
import android.text.TextUtils
import java.lang.Exception
import java.util.*

class AddressDialog(longitude: String, latitude: String) : AppCompatDialogFragment() {
     var latitude: String
    var longitude: String

    init {
        this.longitude = longitude
        this.latitude = latitude
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Address :- ")
                .setMessage("""
    
    ${convertLocationToAddress()}
    
    """.trimIndent())
        return builder.create()
    }

    private fun convertLocationToAddress(): String {
        var addressText: String
        val geocoder = Geocoder(context!!, Locale.getDefault())
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(),
                    1
            )
        } catch (Exception: Exception) {
            addressText = "Error : Coordinates cannot be Converted into Address."
        }
        addressText = if (addresses == null || addresses.size == 0) {
            "Error : Coordinates cannot be Converted into Address."
        } else {
            val address = addresses[0]
            val addressFragments = ArrayList<String?>()
            for (i in 0..address.maxAddressLineIndex) {
                addressFragments.add(address.getAddressLine(i))
            }
            TextUtils.join(System.getProperty("line.separator"),
                    addressFragments)
        }
        return addressText
    }


}