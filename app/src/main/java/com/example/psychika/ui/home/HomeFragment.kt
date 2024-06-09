package com.example.psychika.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.psychika.R
import com.example.psychika.adapter.FeelAdapter
import com.example.psychika.data.Feel
import com.example.psychika.data.local.preference.User
import com.example.psychika.data.local.preference.UserPreference
import com.example.psychika.data.network.Result
import com.example.psychika.databinding.FragmentHomeBinding
import com.example.psychika.ui.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance()
    }
    private val list = ArrayList<Feel>()

    private lateinit var userModel: User
    private lateinit var userPreference: UserPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        userPreference = UserPreference(requireContext())
        userModel = userPreference.getUser()

        if (!userModel.googleAuth) {
            getCurrentUserApi()
        } else {
            getCurrentUserGoogleAuth()
        }


        list.addAll(getListFeel())
        showListFeel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        list.clear()
    }

    private fun getCurrentUserApi() {
        viewModel.getCurrentUserApi("Bearer ${userModel.id}").observe(requireActivity()) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        val response = result.data
                        binding.tvHiUser.text = getString(R.string.hi_user, response.firstName)
                    }

                    is Result.Error -> {
                        showToast(result.error.message)
                    }
                }
            }
        }
    }

    private fun getCurrentUserGoogleAuth() {
        viewModel.getCurrentUserGoogleAuth().observe(requireActivity()) { result ->
            if (result != null) {
                binding.tvHiUser.text = getString(R.string.hi_user, result.firstName)
            } else {
                showToast(getString(R.string.cant_get_user_data_google))
            }
        }
    }

    private fun showListFeel() {
        val layoutManager = GridLayoutManager(requireContext(), list.size)
        val listFeelAdapter = FeelAdapter(list)

        binding.rvListFeel.apply {
            setLayoutManager(layoutManager)
            isNestedScrollingEnabled = false
            adapter = listFeelAdapter
        }
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

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}