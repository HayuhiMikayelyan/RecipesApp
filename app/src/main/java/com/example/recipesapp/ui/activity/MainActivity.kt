 package com.example.recipesapp.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.recipesapp.R
import com.example.recipesapp.databinding.ActivityMainBinding
import com.example.recipesapp.ui.fragment.favorites.FavoritesFragment
import com.example.recipesapp.ui.fragment.home.HomeFragment
import com.example.recipesapp.ui.fragment.profile.ProfileFragment

 class MainActivity : AppCompatActivity() {

     private lateinit var binding: ActivityMainBinding

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)

         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)

         setupClickListener()
         loadFragment(HomeFragment())
     }

     private fun setupClickListener() {
         binding.bottomNavigationView.setOnItemSelectedListener {

             val fragment = when(it.itemId) {
                 R.id.favorites -> {
                     FavoritesFragment()
                 }
                 R.id.profile -> {
                     ProfileFragment()
                 }
                 else -> {
                     HomeFragment()
                 }
             }
             loadFragment(fragment)
             true
         }
     }

     private fun loadFragment(fragment: Fragment) {
         supportFragmentManager
             .beginTransaction()
             .replace(R.id.container, fragment)
             .commit()
     }
 }
