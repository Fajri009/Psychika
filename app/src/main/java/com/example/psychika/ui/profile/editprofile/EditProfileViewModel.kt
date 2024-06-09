package com.example.psychika.ui.profile.editprofile

import androidx.lifecycle.ViewModel
import com.example.psychika.data.network.PsychikaRepository

class EditProfileViewModel(private val psychikaRepository: PsychikaRepository): ViewModel() {
    fun updateCurrentUser(
        token: String,
        firstName: String,
        lastName: String,
        email: String,
    ) = psychikaRepository.updateCurrentUser(token, firstName, lastName, email)
}