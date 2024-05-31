package com.example.psychika.ui.profile.editprofile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.example.psychika.R
import com.example.psychika.databinding.BottomSheetBinding
import com.example.psychika.utils.getImageUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProfileBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetBinding
    private var onImageSelectedListener: OnImageSelectedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        dialog?.setCanceledOnTouchOutside(false)

        binding.apply {
            ivClose.setOnClickListener { dismiss() }
            layoutTakePhoto.setOnClickListener { startCamera() }
            layoutChooseGallery.setOnClickListener { startGallery() }
            layoutDeletePhoto.setOnClickListener { deletePhoto() }
        }

        return binding.root
    }

    private fun startCamera() {
        val imageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(imageUri)
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun deletePhoto() {
        val defaultProfilePic = Uri.parse("android.resource://${requireContext().packageName}/${R.drawable.profile_avatar}")
        onImageSelectedListener?.onImageSelected(defaultProfilePic)
        dismiss()
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            val imageUri = getImageUri(requireContext())
            onImageSelectedListener?.onImageSelected(imageUri)
            dismiss()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onImageSelectedListener?.onImageSelected(uri)
            dismiss()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    fun setOnImageSelectedListener(listener: OnImageSelectedListener) {
        onImageSelectedListener = listener
    }
}