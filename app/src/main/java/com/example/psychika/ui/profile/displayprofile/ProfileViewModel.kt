package com.example.psychika.ui.profile.displayprofile

import androidx.lifecycle.ViewModel
import com.example.psychika.data.repository.PsychikaRepository

class ProfileViewModel(private val psychikaRepository: PsychikaRepository): ViewModel() {
    fun getCurrentUserApi(token: String) =
        psychikaRepository.getCurrentUserApi(token)

    fun getCurrentUserGoogleAuth() =
        psychikaRepository.getCurrentUserGoogleAuth()
}