package com.example.psychika.ui.auth.signup

import androidx.lifecycle.ViewModel
import com.example.psychika.data.network.PsychikaRepository

class SignUpViewModel(private val psychikaRepository: PsychikaRepository) : ViewModel() {
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) =
        psychikaRepository.register(firstName, lastName, email, password)
}