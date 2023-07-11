package com.sarthak.driverlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var regEmail: EditText
    private lateinit var regPassword: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var register: Button
    private lateinit var sRegEmail:String
    private lateinit var sRegPassword:String
    private lateinit var sConfirmPassword:String
    private lateinit var auth:FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth= Firebase.auth
        regEmail = findViewById(R.id.et_reg_username)
        regPassword= findViewById(R.id.et_reg_password)
        confirmPassword = findViewById(R.id.et_reg_confirm_password)
        register = findViewById(R.id.btn_register)



        register.setOnClickListener {

            sRegEmail = regEmail.text.toString().trim()
            sRegPassword = regPassword.text.toString().trim()
            sConfirmPassword = confirmPassword.text.toString().trim()

            if (sRegEmail.isNotEmpty() && sRegPassword.isNotEmpty() && sConfirmPassword.isNotEmpty()) {
                if (sRegPassword == sConfirmPassword) {

                    auth.createUserWithEmailAndPassword(sRegEmail, sRegPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    baseContext, "Enter valid Email id",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //updateUI(null)
                            }
                        }
                }else{
                    confirmPassword.error = "Renter correct password"
                }
            }else if (sRegEmail.isEmpty()){
                regEmail.error="Enter username"
            }else{
               regPassword.error="Enter password"
            }
        }

    }

    private fun updateUI(user: FirebaseUser?) {
        val intent=Intent(this,DashboardActivity::class.java)
        startActivity(intent)

    }

    fun AlreadyHaveAnAccount (view: View) {
        startActivity(Intent(this, LoginActivity::class.java))
    }


}