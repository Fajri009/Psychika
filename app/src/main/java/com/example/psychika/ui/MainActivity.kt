package com.example.psychika.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.psychika.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_chat -> {
                    navController.navigate(R.id.navigation_chat)
                    true
                }
                R.id.navigation_article -> {
                    navController.navigate(R.id.navigation_article)
                    true
                }
                R.id.navigation_profile -> {
                    navController.navigate(R.id.navigation_profile)
                    true
                }
                else -> false
            }
        }

        if (intent.getBooleanExtra("navigateToProfile", false)) {
            navController.navigate(R.id.navigation_profile)
        }
    }
}