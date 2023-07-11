package com.sarthak.driverlogin

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        // Location switch preference
        val locationSwitch = findPreference<SwitchPreferenceCompat>("location_switch")
        locationSwitch?.setOnPreferenceChangeListener { _, newValue ->
            val isLocationEnabled = newValue as Boolean
            // Open location settings if location is disabled
            if (isLocationEnabled) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

            if (!isLocationEnabled) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            true
        }

    }
}