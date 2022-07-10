package com.adesoftware.loginregistrationapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.rengwuxian.materialedittext.MaterialEditText
import java.util.*

private lateinit var firebaseAuth: FirebaseAuth

class LoginRegistrationAppActivity : AppCompatActivity() {

    private lateinit var email: MaterialEditText
    private lateinit var password: MaterialEditText

    private lateinit var registerBtn: Button
    private lateinit var loginBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_registration_app)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        registerBtn = findViewById(R.id.register)
        loginBtn = findViewById(R.id.login)

        firebaseAuth = FirebaseAuth.getInstance()

        registerBtn.setOnClickListener {
            startActivity(Intent(this@LoginRegistrationAppActivity,
                RegisterActivity::class.java))
        }

        loginBtn.setOnClickListener {
            val emailText = email.text.toString()
            val passwordText = password.text.toString()
            if (TextUtils.isEmpty(emailText) or TextUtils.isEmpty(passwordText)) {
                Toast.makeText(this@LoginRegistrationAppActivity,
                    "All fields are required",
                    Toast.LENGTH_SHORT).show()
            } else {
                login(emailText, passwordText)
            }
        }
    }

    private fun login(emailText: String, passwordText: String) {
        lateinit var progressBar: ProgressBar
        progressBar.visibility = View.VISIBLE       //Exception
        firebaseAuth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                val intent = Intent(this, StartActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this,
                    Objects.requireNonNull(task.exception)?.message ?: "Something when wrong!",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}