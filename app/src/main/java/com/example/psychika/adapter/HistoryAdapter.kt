package com.example.psychika.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.psychika.data.entity.DailyAveragePrediction
import com.example.psychika.databinding.ItemRowHistoryBinding

class HistoryAdapter : ListAdapter<DailyAveragePrediction, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallBack

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallback = onItemClickCallBack
    }

    inner class ViewHolder(private val binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chatMessage: DailyAveragePrediction) {
            val percentage = chatMessage.averagePredict * 100
            val formattedPercentage = String.format("%.2f%%", percentage)
            binding.tvDate.text = chatMessage.date
            binding.tvPredict.text = formattedPercentage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatMessage = getItem(position)
        holder.bind(chatMessage)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(chatMessage)
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: DailyAveragePrediction)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DailyAveragePrediction>() {
            override fun areItemsTheSame(oldItem: DailyAveragePrediction, newItem: DailyAveragePrediction): Boolean {
                return oldItem.date == newItem.date
            }

            override fun areContentsTheSame(oldItem: DailyAveragePrediction, newItem: DailyAveragePrediction): Boolean {
                return oldItem == newItem
            }
        }
    }
}