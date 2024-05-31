package com.example.psychika.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.R
import com.example.psychika.databinding.ActivitySignUpBinding
import com.example.psychika.ui.auth.login.LoginActivity
import com.example.psychika.utils.isValidEmail

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            tvLogin.setOnClickListener {
                val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            btnSignup.setOnClickListener {
                signUp()
            }
        }
    }

    private fun signUp() {
        val etFullname = binding.etSignupFullname.text
        val etEmail = binding.etSignupEmail.text
        val etPassword = binding.etSignupPassword.text
        val etConfirmPass = binding.etConfirmPassword.text


        if (etFullname!!.isEmpty() || etEmail!!.isEmpty() || etPassword!!.isEmpty() || etConfirmPass!!.isEmpty()) {
            showToast(R.string.empty_form)
        } else if (!isValidEmail(etEmail) || etPassword.length < 8 || etConfirmPass.length < 8) {
            showToast(R.string.invalid_form)
        } else if (etConfirmPass.toString() != etPassword.toString()) {
            Log.i("test", etConfirmPass.toString())
            Log.i("test", etPassword.toString())
            showToast(R.string.pass_not_match)
        } else {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)

            showToast(R.string.signup_success)
        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
    }
}