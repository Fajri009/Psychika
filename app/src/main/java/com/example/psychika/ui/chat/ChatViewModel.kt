package com.example.psychika.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.psychika.data.entity.ChatMessage
import com.example.psychika.data.local.room.ChatMessageEntity
import com.example.psychika.data.repository.PsychikaRepository
import com.example.psychika.utils.Utils

class ChatViewModel(private val repository: PsychikaRepository) : ViewModel() {
    fun getChatMessageCurrentDate(date: String) =
        repository.getChangeMessagesByDate(date).map { entities ->
            entities.map {
                ChatMessage(it.role, it.message, it.time)
            }
        }

    fun sendChat(messages: List<ChatMessage>) =
        repository.sendChat(messages)

    fun saveToLocalDb(messages: List<ChatMessage>, predict: Double) {
        messages.forEach { message ->
            val entity = ChatMessageEntity(
                role = message.role,
                message = message.content,
                time = message.time,
                date = Utils.getCurrentDate(),
                predict = predict
            )
            repository.insertMessage(entity)
        }
    }

    fun getPredict(text: String) =
        repository.getPredict(text)
}