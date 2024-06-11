package com.example.psychika.data.network

import android.util.Log
import androidx.lifecycle.*
import com.example.psychika.data.network.firebase.UserGoogleAuth
import com.example.psychika.data.network.response.TokenResponse
import com.example.psychika.data.network.response.ErrorsItem
import com.example.psychika.data.network.response.ErrorResponse
import com.example.psychika.data.network.response.SuccessResponse
import com.example.psychika.data.network.response.UnprocessableEntityResponse
import com.example.psychika.data.network.response.UserResponse
import com.example.psychika.data.network.retrofit.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import retrofit2.HttpException

class PsychikaRepository(
    private val apiService: ApiService,
    private val firebaseAuth: FirebaseAuth,
) {
    fun registerApi(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ): LiveData<Result<TokenResponse, ErrorsItem>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.register(firstName, lastName, email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody.toString())
                val errorResponse = Gson().fromJson(errorBody, UnprocessableEntityResponse::class.java).errors
                val errorMessage = errorResponse!!.map { it?.message }
                emit(Result.Error(ErrorsItem(message = errorMessage.toString())))
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

    fun getCurrentUserApi(token: String): LiveData<Result<UserResponse, ErrorResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.getCurrUser(token)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    fun getCurrentUserGoogleAuth(): LiveData<UserGoogleAuth?> =
        liveData {
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                val idToken = currentUser.uid
                val profilePic = currentUser.photoUrl.toString()
                val name = currentUser.displayName ?: ""
                val nameParts = name.split(" ")
                val firstName = nameParts.getOrNull(0)
                val lastName = nameParts.drop(1).joinToString(" ")
                val email = currentUser.email ?: ""
                val userGoogleAuth = UserGoogleAuth(idToken, profilePic, firstName!!, lastName, email)
                emit(userGoogleAuth)
            } else {
                emit(null)
            }
    }

    fun updateCurrentUser(
        token: String,
        firstName: String,
        lastName: String,
        email: String,
    ): LiveData<Result<SuccessResponse, ErrorResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.updateCurrUser(token, firstName, lastName, email)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    fun updatePasswordCurrentUser(
        token: String,
        currPass: String,
        newPass: String
    ): LiveData<Result<SuccessResponse, ErrorResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = apiService.updatePass(token, currPass, newPass)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }
}