package com.example.psychika.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.R
import com.example.psychika.data.network.Result
import com.example.psychika.databinding.ActivitySignUpBinding
import com.example.psychika.ui.ViewModelFactory
import com.example.psychika.ui.auth.login.LoginActivity
import com.example.psychika.utils.isValidEmail

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance()
    }

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
        val etFirstName = binding.etSignupFirstName.text
        val etLastName = binding.etSignupLastName.text
        val etEmail = binding.etSignupEmail.text
        val etPassword = binding.etSignupPassword.text
        val etConfirmPass = binding.etConfirmPassword.text

        if (etFirstName!!.isEmpty() || etLastName!!.isEmpty() || etEmail!!.isEmpty() || etPassword!!.isEmpty() || etConfirmPass!!.isEmpty()) {
            showToast(R.string.empty_form)
        } else if (!isValidEmail(etEmail) || etPassword.length < 8 || etConfirmPass.length < 8) {
            showToast(R.string.invalid_form)
        } else if (etConfirmPass.toString() != etPassword.toString()) {
            showToast(R.string.pass_not_match)
        } else {
            viewModel.register(
                etFirstName.toString(),
                etLastName.toString(),
                etEmail.toString(),
                etPassword.toString(),
            ).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.progressBar.visibility = View.GONE

                            if (result.error.message == "[email must be unique]") {
                                showToast(R.string.email_registered)
                            }
                        }

                        is Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(R.string.try_login)

                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun showToast(message: Int) {
        Toast.makeText(this@SignUpActivity, message, Toast.LENGTH_SHORT).show()
    }
}