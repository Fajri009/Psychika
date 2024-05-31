package com.example.psychika.ui.profile.editprofile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.psychika.databinding.ActivityEditProfileBinding
import com.example.psychika.databinding.PopUpEditBinding
import com.example.psychika.ui.MainActivity

class EditProfileActivity : AppCompatActivity(), OnImageSelectedListener {
    private lateinit var binding: ActivityEditProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            ivBackButton.setOnClickListener {
                finish()
            }
            ivEditProfile.setOnClickListener {
                showBottomSheet()
            }
            btnSaveEdit.setOnClickListener {
                showPopUp()
            }
        }
    }

    private fun showBottomSheet() {
        val bottomSheetFragment = EditProfileBottomSheetFragment()
        bottomSheetFragment.setOnImageSelectedListener(this)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

    private fun showPopUp() {
        val popupBinding = PopUpEditBinding.inflate(layoutInflater)

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
}