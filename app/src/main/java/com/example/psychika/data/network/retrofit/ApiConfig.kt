package com.example.psychika.data.network.retrofit

import com.example.psychika.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private var BASE_URL_AUTH = "http://34.34.223.17:3000/"
        private var BASE_URL_CHATBOT = "https://ollama.sleepingowl.my.id/"
        private var BASE_URL_CLASSIFICATION = "https://psychika.sleepingowl.my.id/"
        private val BASE_URL_MAPS_NEARBY = "https://maps.googleapis.com/maps/api/place/nearbysearch/"

        fun getAuthApiService(): AuthApiService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(AuthApiService::class.java)
        }

        fun getChatbotApiService(): ChatbotApiService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_CHATBOT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ChatbotApiService::class.java)
        }

        fun getClassificationApiService(): ClassificationApiService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_CLASSIFICATION)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ClassificationApiService::class.java)
        }

        fun getMapsNearbyApiService(): NearbyPlacesService {
            val loggingInterceptor =
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_MAPS_NEARBY)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(NearbyPlacesService::class.java)
        }
    }
}