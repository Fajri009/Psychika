package com.example.psychika.ui.article

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.*
import com.example.psychika.databinding.FragmentArticleBinding

class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)

        return binding.root
    }
}