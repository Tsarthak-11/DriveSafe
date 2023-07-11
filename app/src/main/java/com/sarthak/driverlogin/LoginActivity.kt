package com.sarthak.driverlogin

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var Email: EditText
    private lateinit var Password: EditText
    private lateinit var forgotPass:TextView
    private lateinit var login: Button
    private lateinit var sEmail:String
    private lateinit var sPassword:String
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth= Firebase.auth
        Email = findViewById(R.id.et_email)
        Password= findViewById(R.id.et_password)
        login = findViewById(R.id.btn_login)
        forgotPass = findViewById(R.id.tv_forgot_pass)

        forgotPass.setOnClickListener {
            val builder= AlertDialog.Builder(this)
            // builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password,null)
            val forgotPassUsername= view.findViewById<EditText>(R.id.editBox)

            builder.setView(view)
            val dialog= builder.create()

            view.findViewById<Button>(R.id.btn_reset).setOnClickListener {
                forgotPassword(forgotPassUsername)

            }
            view.findViewById<Button>(R.id.btn_cancel).setOnClickListener {
                dialog.dismiss()
            }
            if (dialog.window!=null){
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }
            dialog.show()
        }

        login.setOnClickListener {

            sEmail= Email.text.toString().trim()
            sPassword=Password.text.toString().trim()


            if (sEmail.isNotEmpty() && sPassword.isNotEmpty()) {

                    auth.signInWithEmailAndPassword(sEmail, sPassword)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success")
                                val user = auth.currentUser
                                updateUI(user)
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.exception)

                                Toast.makeText(
                                    baseContext, "Incorrect Credential",
                                    Toast.LENGTH_SHORT
                                ).show()
                                //  updateUI(null)
                            }
                        }
                }else if(sEmail.isEmpty()){
                    Email.error="Enter Email"
                }else{
                    Password.error="Enter Password"
            }
        }

    }
    public override fun onStart() {
        super.onStart()
        auth = Firebase.auth
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            reload(currentUser)
        }
    }

    private fun reload(user: FirebaseUser) {
        val intent = Intent(this,DashboardActivity::class.java)
        startActivity(intent)
    }



    private fun forgotPassword(forgotPassUsername:EditText) {
        if (forgotPassUsername.text.toString().isEmpty())
        {
            forgotPassUsername.error="Enter Email"
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(forgotPassUsername.text.toString()).matches()){
            forgotPassUsername.error="Enter Valid Email"
            return
        }
        Firebase.auth.sendPasswordResetEmail(forgotPassUsername.text.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                   // Log.d(TAG, "Email sent.")
                    Toast.makeText(this,"Check your email",Toast.LENGTH_SHORT)
                }
            }

    }

    private fun updateUI(user: FirebaseUser?) {
           val intent=Intent(this,DashboardActivity::class.java)
           startActivity(intent)
    }

    fun createAccountOnClick(view: View) {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}