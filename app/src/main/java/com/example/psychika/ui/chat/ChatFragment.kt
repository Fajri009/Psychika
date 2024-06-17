package com.example.psychika.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psychika.R
import com.example.psychika.adapter.ChatAdapter
import com.example.psychika.data.ChatMessage
import com.example.psychika.data.network.Result
import com.example.psychika.databinding.FragmentChatBinding
import com.example.psychika.ui.ViewModelFactory
import com.example.psychika.utils.Utils
import com.example.psychika.utils.Utils.convertTimeStampChatApi

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel by viewModels<ChatViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)

        showChat()

        binding.ivSendMessage.setOnClickListener { sendMessage() }

        return binding.root
    }

    private fun showChat() {
        val layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(mutableListOf())

        binding.rvChat.apply {
            setLayoutManager(layoutManager)
            adapter = chatAdapter
        }

        viewModel.chatMessage.observe(requireActivity()) { messages ->
            if (messages.isEmpty()) {
                val defaultBotMessage = ChatMessage(
                    "assistant",
                    getString(R.string.greeting_message),
                    Utils.getCurrentTime()
                )
                chatAdapter.addChatMessage(defaultBotMessage)
                viewModel.saveToLocalDb(listOf(defaultBotMessage))
            } else {
                chatAdapter.updateChatMessages(messages)
                binding.rvChat.smoothScrollToPosition(messages.size - 1)
            }
        }

    }

    private fun sendMessage() {
        val userInput = binding.etUserInputMessage.text.toString()
        if (userInput.isNotEmpty()) {
            val userMessage = ChatMessage("user", userInput, Utils.getCurrentTime())

            chatAdapter.addChatMessage(userMessage)
            binding.rvChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
            viewModel.saveToLocalDb(listOf(userMessage))

            viewModel.sendChat(listOf(userMessage)).observe(requireActivity()) { result ->
                Log.i(TAG, "userInput: $userInput")
                if (result != null) {
                    when (result) {
                        is Result.Loading -> { }
                        is Result.Success -> {
                            val response = result.data
                            Log.i(TAG, "chatbot: ${response.message.content}")
                            val assistantMessage = ChatMessage(
                                "assistant",
                                response.message.content,
                                response.createdAt.convertTimeStampChatApi()
                            )
                            chatAdapter.addChatMessage(assistantMessage)
                            viewModel.saveToLocalDb(listOf(assistantMessage))
                            binding.rvChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
                        }
                        is Result.Error -> {
                            if (result.error.message == "timeout") {
                                showToast(getString(R.string.chat_timeout))
                            } else {
                                showToast(result.error.message)
                            }
                        }
                    }
                }
            }

            chatAdapter.notifyDataSetChanged()
            binding.etUserInputMessage.setText("")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "ChatFragment"
    }
}