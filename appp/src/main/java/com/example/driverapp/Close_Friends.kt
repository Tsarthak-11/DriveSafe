package com.example.driverapp

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.sql.DriverManager.println

class Close_Friends : AppCompatActivity() {
    var add_c: Button? = null
    var add_no1: EditText? = null
    var add_no2: EditText? = null
    var add_no3: EditText? = null
    var cursor: Cursor? = null
    var dataBaseHelper: DataBaseHelper = DataBaseHelper(this@Close_Friends)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_close_friends)
        val actionBar = supportActionBar
        actionBar!!.setBackgroundDrawable(resources.getDrawable(R.drawable.appbar_background))
        add_no1 = findViewById<View>(R.id.phone1) as EditText
        add_no2 = findViewById<View>(R.id.phone2) as EditText
        add_no3 = findViewById<View>(R.id.phone3) as EditText
        val everyone: List<ContactModel> = dataBaseHelper.getEveryone()
        if (!everyone.isEmpty()) {
            for (i in everyone) System.out.println(i.getName())
            try {
                add_no1.setText(everyone[0].getPhone())
                add_no2.setText(everyone[1].getPhone())
                add_no3.setText(everyone[2].getPhone())
            } catch (e: Exception) {
                e.printStackTrace()
            }
            println("executing....")
        }
        onClickAdd1()
        onClickAdd2()
        onClickAdd3()

        //------------Bottom-Navigation---------
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.close_friends
        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.dashboard -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            Dashboard::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.about -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            About::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            Settings::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.description -> {
                    startActivity(
                        Intent(
                            applicationContext,
                            Description::class.java
                        )
                    )
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.close_friends -> return@OnNavigationItemSelectedListener true
            }
            false
        })
    }

    fun onClickAdd1() {
        add_c = findViewById<View>(R.id.addContact1) as Button

        //-----This opens up a list of contacts as a new activity
        try {
            add_c!!.setOnClickListener {
                val i = Intent(Intent.ACTION_PICK)
                i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(i, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onClickAdd2() {
        add_c = findViewById<View>(R.id.addContact2) as Button

        //-----This opens up a list of contacts as a new activity
        try {
            add_c!!.setOnClickListener {
                val i = Intent(Intent.ACTION_PICK)
                i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(i, 2)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onClickAdd3() {
        add_c = findViewById<View>(R.id.addContact3) as Button

        //-----This opens up a list of contacts as a new activity
        try {
            add_c!!.setOnClickListener {
                val i = Intent(Intent.ACTION_PICK)
                i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(i, 3)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //--------if addContact1 button is clicked
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                cursor = contentResolver.query(
                    uri!!,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Contacts.DISPLAY_NAME
                    ),
                    null,
                    null,
                    null
                )
                if (cursor != null && cursor!!.moveToNext()) {
                    val phone = cursor!!.getString(0)
                    val name = cursor!!.getString(1)
                    val contactModel = ContactModel(requestCode, name, phone)
                    add_no1!!.setText(phone)
                    val status: Boolean = dataBaseHelper.addOne(contactModel, requestCode)
                    val ToastMsg =
                        if (status == true) "Contact added successfully" else "Error in adding contact"
                    Toast.makeText(this@Close_Friends, ToastMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //--------if addContact2 button is clicked
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                cursor = contentResolver.query(
                    uri!!,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Contacts.DISPLAY_NAME
                    ),
                    null,
                    null,
                    null
                )
                if (cursor != null && cursor!!.moveToNext()) {
                    val phone = cursor!!.getString(0)
                    val name = cursor!!.getString(1)
                    val contactModel = ContactModel(requestCode, name, phone)
                    add_no2!!.setText(phone)
                    val status: Boolean = dataBaseHelper.addOne(contactModel, requestCode)
                    val ToastMsg =
                        if (status == true) "Contact added successfully" else "Error in adding contact"
                    Toast.makeText(this@Close_Friends, ToastMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //--------if addContact3 button is clicked
        if (requestCode == 3 && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                cursor = contentResolver.query(
                    uri!!,
                    arrayOf(
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Contacts.DISPLAY_NAME
                    ),
                    null,
                    null,
                    null
                )
                if (cursor != null && cursor!!.moveToNext()) {
                    val phone = cursor!!.getString(0)
                    val name = cursor!!.getString(1)
                    val contactModel = ContactModel(requestCode, name, phone)
                    add_no3!!.setText(phone)
                    val status: Boolean = dataBaseHelper.addOne(contactModel, requestCode)
                    val ToastMsg =
                        if (status == true) "Contact added successfully" else "Error in adding contact"
                    Toast.makeText(this@Close_Friends, ToastMsg, Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        var phone1: String? = null
        var phone2: String? = null
        var phone3: String? = null
    }
}