package com.example.psychika.ui.profile.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.data.local.preference.User
import com.example.psychika.data.local.preference.UserPreference
import com.example.psychika.data.network.Result
import com.example.psychika.data.network.response.UserResponse
import com.example.psychika.databinding.ActivityEditProfileBinding
import com.example.psychika.databinding.PopUpChangesBinding
import com.example.psychika.ui.MainActivity
import com.example.psychika.ui.ViewModelFactory

class EditProfileActivity : AppCompatActivity(), OnImageSelectedListener {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel by viewModels<EditProfileViewModel> {
        ViewModelFactory.getInstance()
    }

    private var userModel: User = User()
    private lateinit var userPreference: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        userModel = userPreference.getUser()

        val userResponse = intent.getParcelableExtra<UserResponse>("USER_RESPONSE")

        binding.apply {
            ivBackButton.setOnClickListener { finish() }
            etEditFirstName.setText(userResponse!!.firstName)
            etEditLastName.setText(userResponse.lastName)
            etEditEmail.setText(userResponse.email)
            ivEditProfile.setOnClickListener { showBottomSheet() }
            btnSaveEdit.setOnClickListener { saveChanges() }
        }
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = EditProfileBottomSheetFragment()
        bottomSheetFragment.setOnImageSelectedListener(this)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun saveChanges() {
        val etFirstName = binding.etEditFirstName.text.toString()
        val etLastName = binding.etEditLastName.text.toString()
        val etEmail = binding.etEditEmail.text.toString()

        viewModel.updateCurrentUser(
            "Bearer ${userModel.id}",
            etFirstName,
            etLastName,
            etEmail
        ).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> { }
                    is Result.Success -> {
                        showPopUp()
                    }
                    is Result.Error -> {
                        showToast(result.error.message)
                    }
                }
            }
        }
    }

    private fun showPopUp() {
        val popupBinding = PopUpChangesBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this)
            .setView(popupBinding.root)
            .setCancelable(false)
            .create()
        alertDialog.show()

        popupBinding.btnOk.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("navigateToProfile", true)
            startActivity(intent)
            alertDialog.dismiss()
        }
    }

    override fun onImageSelected(imageUri: Uri) {
        binding.ivProfilePicture.setImageURI(imageUri)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@EditProfileActivity, message, Toast.LENGTH_SHORT).show()
    }
}