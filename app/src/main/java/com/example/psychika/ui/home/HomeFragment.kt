package com.example.psychika.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.example.psychika.R
import com.example.psychika.adapter.FeelAdapter
import com.example.psychika.data.Feel
import com.example.psychika.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val list = ArrayList<Feel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        list.addAll(getListFeel())
        showListFeel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finishAffinity()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun showListFeel() {
        val layoutManager = GridLayoutManager(requireContext(), list.size)
        binding.rvListFeel.layoutManager = layoutManager
        binding.rvListFeel.isNestedScrollingEnabled = false

        val listFeelAdapter = FeelAdapter(list)
        binding.rvListFeel.adapter = listFeelAdapter
    }

    private fun getListFeel(): ArrayList<Feel> {
        val dataIcon = resources.obtainTypedArray(R.array.feel_icon)
        val dataDesc = resources.getStringArray(R.array.feel_desc)

        val listFeel = ArrayList<Feel>()
        for (i in dataDesc.indices) {
            val feel = Feel(dataIcon.getResourceId(i, -1), dataDesc[i])
            listFeel.add(feel)
        }
        return listFeel
    }
}