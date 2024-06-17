package com.example.psychika.ui.profile.changepass

import androidx.lifecycle.ViewModel
import com.example.psychika.data.repository.PsychikaRepository

class ChangePasswordViewModel(private val psychikaRepository: PsychikaRepository): ViewModel() {
    fun updateChangePass(
        token: String,
        currPass: String,
        newPass: String
    ) = psychikaRepository.updatePasswordCurrentUser(token, currPass, newPass)
}