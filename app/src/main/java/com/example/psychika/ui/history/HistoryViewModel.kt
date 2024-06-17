package com.example.psychika.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.psychika.data.entity.ChatMessage
import com.example.psychika.data.repository.PsychikaRepository

class HistoryViewModel(private val repository: PsychikaRepository): ViewModel() {
    fun getAllDateMessages() =
        repository.getAllDateMessages()
}