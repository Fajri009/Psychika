package com.example.psychika.ui.article

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import com.example.psychika.R

class ArticleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false)
    }
}