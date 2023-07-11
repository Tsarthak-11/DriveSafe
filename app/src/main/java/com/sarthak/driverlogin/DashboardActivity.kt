package com.sarthak.driverlogin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() ,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth


     private lateinit var bottomNavigationView : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)





        loadFragment(HomeFragment())



    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadFragment(HomeFragment())
            R.id.nav_profile-> loadFragment(ProfileFragment())
            R.id.nav_maps-> loadFragment(MapsFragment())
            R.id.nav_settings-> loadFragment(SettingsFragment())
            R.id.nav_logout-> {
                Firebase.auth.signOut()
                signOutUser()


            }
        }
        return true
    }

    private fun signOutUser() {
        val intent=Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    interface DenyNotificationListener {
        fun onNotificationDenied(notificationId: Int, timerId: Int)
    }



}
