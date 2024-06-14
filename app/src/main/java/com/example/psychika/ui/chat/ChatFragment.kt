package com.example.psychika.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psychika.adapter.ChatAdapter
import com.example.psychika.data.ChatMessage
import com.example.psychika.databinding.FragmentChatBinding
import com.example.psychika.helper.ChatClassifierHelper
import com.example.psychika.utils.Utils
import org.tensorflow.lite.support.label.Category

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var chatClassifierHelper: ChatClassifierHelper
    private val chatMessages = mutableListOf<ChatMessage>()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)

        showChat()

        chatClassifierHelper = ChatClassifierHelper(
            context = requireContext(),
            classifierListener = object : ChatClassifierHelper.ClassifierListener {
                override fun onResults(results: List<Category>?) {
                    Log.i("hallo", results.toString())

                    results!!.forEach { category ->
                        Log.d("hallo", "Label: ${category.label}, Score: ${category.score}")
                    }
                }

                override fun error(error: String) {
                    Log.i("hallo", error)
                }
            })

        binding.ivSendMessage.setOnClickListener { sendMessage() }

        return binding.root
    }

    private fun showChat() {
        val layoutManager = LinearLayoutManager(requireContext())
        chatAdapter = ChatAdapter(chatMessages)

        binding.rvChat.apply {
            setLayoutManager(layoutManager)
            adapter = chatAdapter
        }
    }

    private fun sendMessage() {
        val userInput = binding.etUserInputMessage.text.toString()
        if (userInput.isNotEmpty()) {
            val currentTime = Utils.getCurrentTime()
            chatMessages.add(ChatMessage(userInput, currentTime, true))
            chatAdapter.notifyDataSetChanged()

            chatClassifierHelper.classifyChat(userInput)
            binding.etUserInputMessage.setText("")
        }
    }
}