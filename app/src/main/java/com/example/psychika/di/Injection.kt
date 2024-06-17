package com.example.psychika.di

import android.content.Context
import com.example.psychika.data.local.room.ChatMessageDatabase
import com.example.psychika.data.repository.PsychikaRepository
import com.example.psychika.data.network.retrofit.ApiConfig
import com.google.firebase.auth.FirebaseAuth

object Injection {
    fun provideRepository(context: Context): PsychikaRepository {
        val psychikaApiService = ApiConfig.getPsychikaApiService()
        val ollamaApiService = ApiConfig.getOllamaApiService()
        val firebaseAuth = FirebaseAuth.getInstance()
        val chatMessageDao = ChatMessageDatabase.getDatabase(context).chatMessageDao()
        return PsychikaRepository(psychikaApiService, ollamaApiService, firebaseAuth, chatMessageDao)
    }
}