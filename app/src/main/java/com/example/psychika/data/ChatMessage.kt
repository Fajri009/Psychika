package com.example.psychika.data

data class ChatbotRequest(
    val model: String,
    val messages: List<ChatMessage>,
    val stream: Boolean
)

data class ChatMessage(
    val role: String,
    val content: String,
    val time: String
)