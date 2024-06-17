package com.example.psychika.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.psychika.data.ChatMessage
import com.example.psychika.data.local.room.ChatMessageEntity
import com.example.psychika.data.network.Result
import com.example.psychika.data.network.response.ChatbotResponse
import com.example.psychika.data.network.response.ErrorChatbotResponse
import com.example.psychika.data.repository.PsychikaRepository

class ChatViewModel(private val repository: PsychikaRepository) : ViewModel() {
    val chatMessage: LiveData<List<ChatMessage>> = repository.getChatMessage().map { entities ->
        entities.map {
            ChatMessage(it.role, it.message, it.time)
        }
    }

    fun sendChat(messages: List<ChatMessage>): LiveData<Result<ChatbotResponse, ErrorChatbotResponse>> {
        return repository.sendChat(messages)
    }

    fun saveToLocalDb(messages: List<ChatMessage>) {
        messages.forEach { message ->
            val entity = ChatMessageEntity(
                role = message.role,
                message = message.content,
                time = message.time
            )
            repository.insertMessage(entity)
        }
    }
}