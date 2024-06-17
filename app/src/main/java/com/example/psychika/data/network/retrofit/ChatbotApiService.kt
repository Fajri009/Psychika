package com.example.psychika.data.network.retrofit

import com.example.psychika.data.entity.ChatbotRequest
import com.example.psychika.data.network.response.ChatbotResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatbotApiService {
    @POST("api/chat")
    suspend fun sendChat(
        @Body request: ChatbotRequest
    ): ChatbotResponse
}