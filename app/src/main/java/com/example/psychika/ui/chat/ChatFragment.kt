package com.example.psychika.ui.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.psychika.R
import com.example.psychika.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private var doubleBack = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)

        handlingBackPress()

        return binding.root
    }

    private fun handlingBackPress() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBack) {
                    requireActivity().finishAffinity()
                    return
                }

                doubleBack = true
                Toast.makeText(requireContext(), R.string.press_back_again, Toast.LENGTH_SHORT).show()

                handler.postDelayed({
                    doubleBack = false
                }, 2000)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}