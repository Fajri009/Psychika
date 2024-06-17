package com.example.psychika.ui.profile.editprofile

import androidx.lifecycle.ViewModel
import com.example.psychika.data.repository.PsychikaRepository

class EditProfileViewModel(private val repository: PsychikaRepository): ViewModel() {
    fun updateCurrentUser(
        token: String,
        firstName: String,
        lastName: String,
        email: String,
    ) = repository.updateCurrentUser(token, firstName, lastName, email)
}