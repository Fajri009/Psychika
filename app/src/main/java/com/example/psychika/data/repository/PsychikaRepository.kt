package com.example.psychika.data.repository

import android.util.Log
import androidx.lifecycle.*
import com.example.psychika.data.ChatMessage
import com.example.psychika.data.ChatbotRequest
import com.example.psychika.data.ErrorMessage
import com.example.psychika.data.local.room.ChatMessageDao
import com.example.psychika.data.local.room.ChatMessageEntity
import com.example.psychika.data.network.Result
import com.example.psychika.data.network.firebase.UserGoogleAuth
import com.example.psychika.data.network.response.ChatbotResponse
import com.example.psychika.data.network.response.ErrorChatbotResponse
import com.example.psychika.data.network.response.TokenResponse
import com.example.psychika.data.network.response.ErrorsItem
import com.example.psychika.data.network.response.ErrorResponse
import com.example.psychika.data.network.response.SuccessResponse
import com.example.psychika.data.network.response.UnprocessableEntityResponse
import com.example.psychika.data.network.response.UserResponse
import com.example.psychika.data.network.retrofit.OllamaApiService
import com.example.psychika.data.network.retrofit.PsychikaApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException
import java.net.SocketTimeoutException

class PsychikaRepository(
    private val psychikaApiService: PsychikaApiService,
    private val ollamaApiService: OllamaApiService,
    private val firebaseAuth: FirebaseAuth,
    private val chatMessageDao: ChatMessageDao
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
                val response = psychikaApiService.register(firstName, lastName, email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody.toString())
                val errorResponse =
                    Gson().fromJson(errorBody, UnprocessableEntityResponse::class.java).errors
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
                val response = psychikaApiService.login(email, password)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    fun loginWithGoogle(idToken: String): LiveData<Result<UserGoogleAuth, ErrorMessage>> =
        liveData {
            emit(Result.Loading)

            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                firebaseAuth.signInWithCredential(credential).await()
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val idToken = currentUser.uid
                    val profilePic = currentUser.photoUrl.toString()
                    val name = currentUser.displayName ?: ""
                    val nameParts = name.split(" ")
                    val firstName = nameParts.getOrNull(0)
                    val lastName = nameParts.drop(1).joinToString(" ")
                    val email = currentUser.email ?: ""
                    val userGoogleAuth =
                        UserGoogleAuth(idToken, profilePic, firstName, lastName, email)
                    emit(Result.Success(userGoogleAuth))
                } else {
                    emit(Result.Error(ErrorMessage("Failed to retrieve user information")))
                }
            } catch (e: Exception) {
                emit(Result.Error(ErrorMessage(e.message ?: "Unknown error")))
            }
        }

    fun getCurrentUserApi(token: String): LiveData<Result<UserResponse, ErrorResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val response = psychikaApiService.getCurrUser(token)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    fun getCurrentUserGoogleAuth(): LiveData<Result<UserGoogleAuth, ErrorMessage>> =
        liveData {
            emit(Result.Loading)

            try {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val idToken = currentUser.uid
                    val profilePic = currentUser.photoUrl.toString()
                    val name = currentUser.displayName ?: ""
                    val nameParts = name.split(" ")
                    val firstName = nameParts.getOrNull(0)
                    val lastName = nameParts.drop(1).joinToString(" ")
                    val email = currentUser.email ?: ""
                    val userGoogleAuth =
                        UserGoogleAuth(idToken, profilePic, firstName!!, lastName, email)
                    emit(Result.Success(userGoogleAuth))
                }
            } catch (e: Exception) {
                emit(Result.Error(ErrorMessage(e.message ?: "Unknown error")))
            }
        }

    fun sendChat(message: List<ChatMessage>): LiveData<Result<ChatbotResponse, ErrorChatbotResponse>> =
        liveData {
            emit(Result.Loading)

            try {
                val request = ChatbotRequest("psychika1", message, false)
                val response = ollamaApiService.sendChat(request)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, ErrorChatbotResponse::class.java)
                val errorMessage = errorResponse.message
                Log.i(TAG, "errorSendChat: $errorMessage")
                emit(Result.Error(ErrorChatbotResponse(errorMessage)))
            } catch (e: SocketTimeoutException) {
                emit(Result.Error(ErrorChatbotResponse(e.message ?: "Unknown error")))
            }
        }

    fun insertMessage(chatMessage: ChatMessageEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            chatMessageDao.insertMessage(chatMessage)
        }
    }

    fun getChatMessage(): LiveData<List<ChatMessageEntity>> {
        return chatMessageDao.getAllMessages()
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
                val response = psychikaApiService.updateCurrUser(token, firstName, lastName, email)
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
                val response = psychikaApiService.updatePass(token, currPass, newPass)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                Log.i("errorBody", errorBody!!)
                val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
                val errorMessage = errorResponse.message
                emit(Result.Error(ErrorResponse(errorMessage)))
            }
        }

    companion object {
        const val TAG = "PsychikaRepository"
    }
}