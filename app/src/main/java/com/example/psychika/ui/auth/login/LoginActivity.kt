package com.example.psychika.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.R
import com.example.psychika.databinding.ActivityLoginBinding
import com.example.psychika.ui.MainActivity
import com.example.psychika.ui.auth.forgotpass.ForgotPasswordActivity
import com.example.psychika.ui.auth.signup.SignUpActivity
import com.example.psychika.utils.isValidEmail

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tvButton()

        login()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun tvButton() {
        binding.tvForgotPass.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login() {
        val etLoginEmail = binding.etLoginEmail.text
        val etLoginPassword = binding.etLoginPassword.text

        binding.btnLogin.setOnClickListener {
            if (etLoginEmail!!.isEmpty() || etLoginPassword!!.isEmpty()) {
                showToast(R.string.empty_form)
            } else if (!isValidEmail(etLoginEmail.toString()) || etLoginPassword.length < 8) {
                showToast(R.string.invalid_form)
            } else {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }
}