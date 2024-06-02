package com.example.psychika.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.lifecycleScope
import com.example.psychika.R
import com.example.psychika.data.local.preference.User
import com.example.psychika.data.local.preference.UserPreference
import com.example.psychika.databinding.ActivityLoginBinding
import com.example.psychika.ui.MainActivity
import com.example.psychika.ui.auth.forgotpass.ForgotPasswordActivity
import com.example.psychika.ui.auth.signup.SignUpActivity
import com.example.psychika.utils.isValidEmail
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var doubleBack = false
    private val handler = Handler(Looper.getMainLooper())

    private var userModel: User = User()
    private lateinit var userPreference: UserPreference

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)

        auth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.apply {
            tvForgotPass.setOnClickListener {
                val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
            tvSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                startActivity(intent)
            }
            layoutRemember.setOnClickListener {
                cbRemember.isChecked = !cbRemember.isChecked
            }
            cbRemember.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    userModel.email = etLoginEmail.toString()
                } else {
                    userModel.email = ""
                }
            }
            btnLogin.setOnClickListener {
                login()
            }
            btnGoogle.setOnClickListener {
                loginWithGoogle()
            }
        }
    }

    override fun onBackPressed() {
        if (doubleBack) {
            finishAffinity()
            super.onBackPressed()
            return
        }

        doubleBack = true
        Toast.makeText(this@LoginActivity, R.string.press_back_again, Toast.LENGTH_SHORT).show()

        handler.postDelayed({
            doubleBack = false
        }, 2000)
    }

    private fun login() {
        val etLoginEmail = binding.etLoginEmail.text
        val etLoginPassword = binding.etLoginPassword.text

        if (etLoginEmail!!.isEmpty() || etLoginPassword!!.isEmpty()) {
            showToast(R.string.empty_form)
        } else if (!isValidEmail(etLoginEmail.toString()) || etLoginPassword.length < 8) {
            showToast(R.string.invalid_form)
        } else {
            userPreference.setUser(userModel)

            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginWithGoogle() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)

        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    request = request,
                    context = this@LoginActivity,
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)
//        if (requestCode == RC_SIGN_IN) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                val account = task.getResult(ApiException::class.java)!!
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e)
//                showToast(R.string.login_failed)
//            }
//        }
//    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    userModel.email = user?.email ?: ""
                    userPreference.setUser(userModel)
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    showToast(R.string.login_failed)
                }
            }
    }

    private fun showToast(message: Int) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}