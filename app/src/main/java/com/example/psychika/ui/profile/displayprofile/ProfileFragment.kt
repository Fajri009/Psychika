package com.example.psychika.ui.profile.displayprofile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.example.psychika.R
import com.example.psychika.databinding.FragmentProfileBinding
import com.example.psychika.ui.auth.login.LoginActivity
import com.example.psychika.ui.profile.editprofile.EditProfileActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private var doubleBack = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        handlingBackPress()

        binding.apply {
            btnEditProfile.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
            btnLogout.setOnClickListener {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        }

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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}