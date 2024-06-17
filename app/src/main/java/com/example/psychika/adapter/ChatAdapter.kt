package com.example.psychika.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.psychika.R
import com.example.psychika.data.ChatMessage
import com.example.psychika.databinding.ItemBotChatBinding
import com.example.psychika.databinding.ItemUserChatBinding

class ChatAdapter(private var chatMessages: MutableList<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class UserChatViewHolder(private val binding: ItemUserChatBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind (chatMessage: ChatMessage) {
            binding.apply {
                tvMessage.text = chatMessage.content
                tvTime.text = chatMessage.time
            }
        }
    }

    inner class BotChatViewHolder(private val binding: ItemBotChatBinding) : RecyclerView.ViewHolder(binding.root)  {
        fun bind (chatMessage: ChatMessage) {
            binding.apply {
                tvMessage.text = chatMessage.content
                tvTime.text = chatMessage.time
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].role == "user") {
            R.layout.item_user_chat
        } else {
            R.layout.item_bot_chat
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.item_user_chat) {
            val binding = ItemUserChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserChatViewHolder(binding)
        } else {
            val binding = ItemBotChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BotChatViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatMessage = chatMessages[position]
        when (holder) {
            is UserChatViewHolder -> holder.bind(chatMessage)
            is BotChatViewHolder -> holder.bind(chatMessage)
        }
    }

    override fun getItemCount(): Int = chatMessages.size

    fun addChatMessage(message: ChatMessage) {
        chatMessages.add(message)
        notifyItemInserted(chatMessages.size - 1)
    }

    fun updateChatMessages(messages: List<ChatMessage>) {
        chatMessages.clear()
        chatMessages.addAll(messages)
        notifyDataSetChanged()
    }
}