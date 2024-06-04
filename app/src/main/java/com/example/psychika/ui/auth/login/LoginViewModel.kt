package com.example.psychika.ui.auth.login

import androidx.lifecycle.ViewModel
import com.example.psychika.data.network.PsychikaRepository

class LoginViewModel(private val psychikaRepository: PsychikaRepository): ViewModel() {
    fun login(email: String, password: String) =
        psychikaRepository.login(email, password)

    fun loginWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        psychikaRepository.loginWithGoogle(idToken, onResult)
    }
}