package com.adesoftware.loginregistrationapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.rengwuxian.materialedittext.MaterialEditText
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var userName: MaterialEditText
    private lateinit var emailAddress: MaterialEditText
    private lateinit var password: MaterialEditText
    private lateinit var mobile: MaterialEditText
    private lateinit var radioGroup: RadioGroup

    private lateinit var registerBtn: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        var toolbar: Toolbar? = null
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            startActivity(Intent(this@RegisterActivity,
                LoginRegistrationAppActivity::class.java)) })
        firebaseAuth = FirebaseAuth.getInstance()

        userName = findViewById(R.id.user_name)
        emailAddress = findViewById(R.id.email_reg)
        password = findViewById(R.id.password_reg)
        mobile = findViewById(R.id.mobile)
        radioGroup = findViewById(R.id.radio_button)
        registerBtn = findViewById(R.id.register_reg)
        progressBar = findViewById(R.id.progressBar_reg)

        registerBtn.setOnClickListener {
            val nameUser = userName.text.toString()
            val email = emailAddress.text.toString()
            val passwordText = password.text.toString()
            val mobileText = mobile.text.toString()
            val checkId: Int = radioGroup.checkedRadioButtonId
            val selectedGender: RadioButton = radioGroup.findViewById(checkId)
            if (selectedGender == null) {
                Toast.makeText(this@RegisterActivity, "Select gender please", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val gender: String = selectedGender.text.toString()
                if (TextUtils.isEmpty(nameUser) || TextUtils.isEmpty(email) || TextUtils.isEmpty(
                        passwordText) ||
                    TextUtils.isEmpty(mobileText)
                ) {
                    Toast.makeText(this@RegisterActivity,
                        "All fields are required",
                        Toast.LENGTH_SHORT).show()
                } else {
                    register(nameUser, email, passwordText, mobileText, gender)
                }
            }
        }
    }

    private fun register(userName: String, email: String, passwordText: String,
                         mobileText: String, gender: String) {
        progressBar.visibility = View.VISIBLE
        firebaseAuth.createUserWithEmailAndPassword(email, passwordText).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val rUser: FirebaseUser? = firebaseAuth.currentUser
                val userId: String = rUser!!.uid
                databaseReference =
                    FirebaseDatabase.getInstance().getReference("Users").child(userId)
                val hashMap: HashMap<String, String> = HashMap()
                hashMap.put("userId", userId)
                hashMap.put("username", userName)
                hashMap.put("email", email)
                hashMap.put("password", passwordText)
                hashMap.put("mobile", mobileText)
                hashMap.put("gender", gender)
                hashMap.put("imageUrl", "default")
                databaseReference.setValue(hashMap).addOnCompleteListener {
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        val intent: Intent = Intent(this@RegisterActivity,
                            LoginRegistrationAppActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@RegisterActivity,
                            Objects.requireNonNull(task.exception)?.message ?: "Something when wrong!",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                progressBar.visibility = View.GONE
                Toast.makeText(this@RegisterActivity,
                    Objects.requireNonNull(task.exception)?.message ?: "", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}