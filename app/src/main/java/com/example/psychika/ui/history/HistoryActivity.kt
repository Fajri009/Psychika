package com.example.psychika.ui.history

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.psychika.adapter.HistoryAdapter
import com.example.psychika.data.entity.DailyAveragePrediction
import com.example.psychika.data.local.preference.user.User
import com.example.psychika.data.local.preference.user.UserPreference
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

    private lateinit var userModel: User
    private lateinit var userPreference: UserPreference
    private lateinit var userId: String

    private var isDialogShowing = false
    private var dialogDate: String? = null
    private var alertDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference(this)
        userModel = userPreference.getUser()
        userId = userModel.id!!

        if (savedInstanceState != null) {
            isDialogShowing = savedInstanceState.getBoolean("isDialogShowing")
            dialogDate = savedInstanceState.getString("dialogDate")
        }

        setupRecyclerView()
        setDataChatHistoryDate()

        binding.ivBack.setOnClickListener { finish() }

        if (isDialogShowing && dialogDate != null) {
            showPopUp(dialogDate!!)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("isDialogShowing", isDialogShowing)
        outState.putString("dialogDate", dialogDate)
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
                showPopUp(data.date)
            }
        })
    }

    private fun setDataChatHistoryDate() {
        viewModel.getAllDateMessages(userId).observe(this) {
            historyAdapter.submitList(it)
        }
    }

    private fun showPopUp(date: String) {
        val popUpBinding = PopUpHistoryBinding.inflate(layoutInflater)

        alertDialog = AlertDialog.Builder(this)
            .setView(popUpBinding.root)
            .setCancelable(false)
            .create()
        alertDialog?.show()

        popUpBinding.apply {
            btnBack.setOnClickListener { alertDialog?.dismiss() }
            btnSeeHistory.setOnClickListener {
                isDialogShowing = false
                dialogDate = null
                val intent = Intent(this@HistoryActivity, ChatbotHistory::class.java)
                intent.putExtra("DATE_CHAT_HISTORY", date)
                startActivity(intent)
                alertDialog?.dismiss()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
    }
}