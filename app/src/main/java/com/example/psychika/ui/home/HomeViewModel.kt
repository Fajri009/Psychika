package com.example.psychika.ui.home

import androidx.lifecycle.ViewModel
import com.example.psychika.data.network.PsychikaRepository

class HomeViewModel(private val psychikaRepository: PsychikaRepository): ViewModel() {
    fun getCurrentUserApi(token: String) =
        psychikaRepository.getCurrentUserApi(token)

    fun getCurrentUserGoogleAuth() =
        psychikaRepository.getCurrentUserGoogleAuth()
}