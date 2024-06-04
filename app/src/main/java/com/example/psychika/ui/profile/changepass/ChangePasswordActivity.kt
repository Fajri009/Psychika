package com.example.psychika.ui.profile.changepass

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ivBackButton.setOnClickListener { finish() }
        }
    }
}