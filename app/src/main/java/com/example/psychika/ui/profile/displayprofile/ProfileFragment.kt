package com.example.psychika.ui.profile.displayprofile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import com.example.psychika.databinding.FragmentProfileBinding
import com.example.psychika.ui.auth.login.LoginActivity
import com.example.psychika.ui.profile.editprofile.EditProfileActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        buttonClicked()

        return binding.root
    }

    private fun buttonClicked() {
        binding.btnEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}