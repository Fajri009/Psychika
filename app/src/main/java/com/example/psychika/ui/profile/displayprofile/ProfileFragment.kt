package com.example.psychika.ui.profile.displayprofile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.*
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import com.example.psychika.data.local.preference.User
import com.example.psychika.data.local.preference.UserPreference
import com.example.psychika.databinding.FragmentProfileBinding
import com.example.psychika.ui.auth.login.LoginActivity
import com.example.psychika.ui.profile.changepass.ChangePasswordActivity
import com.example.psychika.ui.profile.editprofile.EditProfileActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    private var userModel: User = User()
    private lateinit var userPreference: UserPreference

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        auth = FirebaseAuth.getInstance()

        userPreference = UserPreference(requireContext())

        binding.apply {
            btnHistory.setOnClickListener {  }
            btnEditProfile.setOnClickListener {
                val intent = Intent(requireContext(), EditProfileActivity::class.java)
                startActivity(intent)
            }
            btnChangePass.setOnClickListener {
                val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
                startActivity(intent)
            }
            btnLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
            btnLogout.setOnClickListener {
                logout()
            }
        }

        return binding.root
    }

    private fun logout() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(requireContext())

            auth.signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())

            userModel.id = ""
            userPreference.setUser(userModel)

            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
}