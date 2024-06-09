package com.example.psychika.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.psychika.data.network.PsychikaRepository
import com.example.psychika.di.Injection
import com.example.psychika.ui.auth.login.LoginViewModel
import com.example.psychika.ui.auth.signup.SignUpViewModel
import com.example.psychika.ui.home.HomeViewModel
import com.example.psychika.ui.profile.changepass.ChangePasswordViewModel
import com.example.psychika.ui.profile.displayprofile.ProfileViewModel
import com.example.psychika.ui.profile.editprofile.EditProfileViewModel

class ViewModelFactory private constructor(private val psychikaRepository: PsychikaRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(psychikaRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(psychikaRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(psychikaRepository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(psychikaRepository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(psychikaRepository) as T
            }
            modelClass.isAssignableFrom(ChangePasswordViewModel::class.java) -> {
                ChangePasswordViewModel(psychikaRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository())
            }.also { instance = it }
    }
}