package com.example.psychika.di

import com.example.psychika.data.network.PsychikaRepository
import com.example.psychika.data.network.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth

object Injection {
    fun provideRepository(): PsychikaRepository {
        val apiService = ApiConfig.getApiService()
        val firebaseAuth = FirebaseAuth.getInstance()
        return PsychikaRepository(apiService, firebaseAuth)
    }
}