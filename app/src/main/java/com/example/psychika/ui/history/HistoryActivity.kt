package com.example.psychika.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psychika.adapter.HistoryAdapter
import com.example.psychika.data.entity.DailyAveragePrediction
import com.example.psychika.databinding.ActivityHistoryBinding
import com.example.psychika.databinding.PopUpHistoryBinding
import com.example.psychika.ui.ViewModelFactory
import com.example.psychika.ui.history.chatbot.ChatbotHistory

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setDataChatHistoryDate()

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this@HistoryActivity)
        historyAdapter = HistoryAdapter()

        binding.rvListHistory.apply {
            setLayoutManager(layoutManager)
            adapter = historyAdapter
        }

        historyAdapter.setOnItemClickCallBack(object : HistoryAdapter.OnItemClickCallBack {
            override fun onItemClicked(data: DailyAveragePrediction) {
                showPopUp()
            }
        })
    }

    private fun setDataChatHistoryDate() {
        viewModel.getAllDateMessages().observe(this) {
            historyAdapter.submitList(it)
        }
    }

    private fun showPopUp() {
        val popUpBinding = PopUpHistoryBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(this)
            .setView(popUpBinding.root)
            .setCancelable(false)
            .create()
        alertDialog.show()

        popUpBinding.apply {
            btnBack.setOnClickListener { finish() }
            btnSeeHistory.setOnClickListener {
                val intent = Intent(this@HistoryActivity, ChatbotHistory::class.java)
                startActivity(intent)
                alertDialog.dismiss()
            }
        }
    }
}