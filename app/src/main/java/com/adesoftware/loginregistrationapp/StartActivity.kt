package com.adesoftware.loginregistrationapp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView

class StartActivity : AppCompatActivity() {

    private lateinit var userName: TextView
    private lateinit var circleImageView: CircleImageView

    private lateinit var firebaseUser: FirebaseUser
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        val toolbar: Toolbar = findViewById(R.id.toolbar_profile)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        userName = findViewById(R.id.user_name_profile)
        circleImageView = findViewById(R.id.profile_image)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseUser = firebaseAuth.currentUser!!
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
            .child(firebaseUser.uid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val usersData = snapshot.getValue(UsersData::class.java)
                usersData != null
                userName.text = usersData?.username ?: "Joe"
                if (usersData?.imageUrl?.equals("default") == true) {
                    circleImageView.setImageResource(R.drawable.ic_launcher_background)
                } else {
                    if (usersData != null) {
                        Glide.with(applicationContext).load(usersData.imageUrl).into(circleImageView)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@StartActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }
}