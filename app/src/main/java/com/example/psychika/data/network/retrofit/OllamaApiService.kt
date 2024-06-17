package com.example.psychika.data.network.retrofit

import com.example.psychika.data.ChatbotRequest
import com.example.psychika.data.network.response.ChatbotResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface OllamaApiService {
    @POST("api/chat")
    suspend fun sendChat(
        @Body request: ChatbotRequest
    ): ChatbotResponse
}