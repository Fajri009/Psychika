package com.example.psychika.ui.article

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.psychika.R
import com.example.psychika.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private var doubleBack = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(layoutInflater)

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