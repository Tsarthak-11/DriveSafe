package com.sarthak.driverlogin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment()  {

    lateinit var addinformation : Button
    lateinit var viewinformation : Button
    lateinit var button1 :ImageView
    lateinit var button2 :ImageView
    lateinit var button3 :ImageView
    lateinit var number1 :TextView
    lateinit var name1 : TextView
    lateinit var number2 :TextView
    lateinit var name2 : TextView
    lateinit var number3 :TextView
    lateinit var name3 : TextView
    lateinit var uploadcontacts : Button
    var databaseReference: DatabaseReference? = null
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)


        addinformation = view.findViewById(R.id.addinformation)
        viewinformation = view.findViewById(R.id.viewinformation)
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        button1 = view.findViewById(R.id.contact1)
        number1 = view.findViewById(R.id.num1)
        name1 = view.findViewById(R.id.name1)
        button2 = view.findViewById(R.id.contact2)
        number2 = view.findViewById(R.id.num2)
        name2 = view.findViewById(R.id.name2)
        button3 = view.findViewById(R.id.contact3)
        number3 = view.findViewById(R.id.num3)
        name3 = view.findViewById(R.id.name3)
        uploadcontacts = view.findViewById(R.id.btn_upload_contacts)
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Emergency_Contacts")
        number1.setText(sharedPreferences.getString("number1", ""))
        name1.setText(sharedPreferences.getString("name1", ""))
        number2.setText(sharedPreferences.getString("number2", ""))
        name2.setText(sharedPreferences.getString("name2", ""))
        number3.setText(sharedPreferences.getString("number3", ""))
        name3.setText(sharedPreferences.getString("name3", ""))


        addinformation.setOnClickListener {
            val intent=Intent(requireContext(),AddInfoActivity::class.java)
            startActivity(intent)
        }

        viewinformation.setOnClickListener {
            val intent=Intent(requireContext(),ViewInfoActivity::class.java)
            startActivity(intent)
        }

        button1.setOnClickListener {
            var i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i,111)
        }

        button2.setOnClickListener {
            var i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i,112)
        }

        button3.setOnClickListener {
            var i = Intent(Intent.ACTION_PICK)
            i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(i,113)
        }

        uploadcontacts.setOnClickListener {

            val editor = sharedPreferences.edit()
            editor.putString("number1", number1.text.toString())
            editor.putString("name1", name1.text.toString())
            editor.putString("number2", number2.text.toString())
            editor.putString("name2", name2.text.toString())
            editor.putString("number3", number3.text.toString())
            editor.putString("name3", name3.text.toString())
            editor.apply()

            val n1: String = number1!!.text.toString().trim()
            val na1: String = name1!!.text.toString().trim()
            val n2: String = number2!!.text.toString().trim()
            val na2: String = name2!!.text.toString().trim()
            val n3: String = number3!!.text.toString().trim()
            val na3: String = name3!!.text.toString().trim()
            if (!(TextUtils.isEmpty(n1) || TextUtils.isEmpty(na1) || TextUtils.isEmpty(n2) || TextUtils.isEmpty(na2) || TextUtils.isEmpty(n3) || TextUtils.isEmpty(na3))
            ) {
               databaseReference!!.push()
                val contactsInfo = EmergencyContacts(n1,na1,
                                                     n2,na2,
                                                     n3,na3)
                databaseReference!!.setValue(contactsInfo)
                Toast.makeText(activity, "Data Added Successfully!!!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Input Field Cannot be Blank!!!", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==111 && resultCode== Activity.RESULT_OK){
            var contacturi : Uri = data?.data ?: return
            var cols : Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            var rs = context?.contentResolver?.query(contacturi,cols,null,null,null)

            if (rs?.moveToFirst()!!){
                number1.setText(rs.getString(0))
                name1.setText(rs.getString(1))
            }

        }
        if (requestCode==112 && resultCode== Activity.RESULT_OK){
            var contacturi : Uri = data?.data ?: return
            var cols : Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            var rs = context?.contentResolver?.query(contacturi,cols,null,null,null)

            if (rs?.moveToFirst()!!){
                number2.setText(rs.getString(0))
                name2.setText(rs.getString(1))
            }

        }

        if (requestCode==113 && resultCode== Activity.RESULT_OK){
            var contacturi : Uri = data?.data ?: return
            var cols : Array<String> = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            var rs = context?.contentResolver?.query(contacturi,cols,null,null,null)

            if (rs?.moveToFirst()!!){
                number3.setText(rs.getString(0))
                name3.setText(rs.getString(1))
            }

        }

    }




}