package com.example.recipesapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipesapp.R
import com.google.firebase.auth.FirebaseAuth

class StartActivity : AppCompatActivity() {
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        firebaseAuth= FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}