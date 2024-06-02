package com.example.psychika.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.R
import com.example.psychika.data.local.preference.User
import com.example.psychika.data.local.preference.UserPreference
import com.example.psychika.ui.MainActivity
import com.example.psychika.ui.auth.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var userModel: User
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        userPreference = UserPreference(this)
        userModel = userPreference.getUser()

        Handler(Looper.getMainLooper()).postDelayed({
            Log.i("userModel", userModel.email!!.toString())
            if (userModel.email == "") {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashActivity, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, SPLASH_TIME_OUT)
    }

    companion object {
        private const val SPLASH_TIME_OUT: Long = 3000
    }
}