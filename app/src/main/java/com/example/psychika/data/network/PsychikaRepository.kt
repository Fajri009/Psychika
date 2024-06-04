package com.example.psychika.data.network

import android.util.Log
import androidx.lifecycle.*
import com.example.psychika.data.network.response.TokenResponse
import com.example.psychika.data.network.response.ErrorsItem
import com.example.psychika.data.network.response.ErrorResponse
import com.example.psychika.data.network.retrofit.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import retrofit2.HttpException

class PsychikaRepository(
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth
) {
    fun register(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): LiveData<Result<TokenResponse, ErrorsItem>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.register(firstName, lastName, email, password)
                Log.i("errorBody", response.toString())
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorsItem::class.java)
                val errorMessage = errorResponse.message ?: "Unknown error"
                emit(Result.Error(ErrorsItem(message = errorMessage)))
            }
        }

    fun login(
        email: String,
        password: String
    ): LiveData<Result<TokenResponse, ErrorResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    fun loginWithGoogle(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                onResult(task.isSuccessful)
            }
            .addOnFailureListener { exception ->
                onResult(false)
            }
    }
}