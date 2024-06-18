package com.dicoding.yogascan.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dicoding.yogascan.R
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.response.SigninResponse
import com.dicoding.yogascan.databinding.FragmentProfileBinding
import com.dicoding.yogascan.ui.signin.SignInActivity

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ProfileViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signout.setOnClickListener {
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
        }

        viewModel.getSession().observe(viewLifecycleOwner, Observer { user: SigninResponse? ->
            user?.uid?.let { uid ->
                getData(uid)
            }
        })
    }

    private fun getData(uid: String) {
        viewModel.getProfile(uid).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> {
                    showLoading(true)
                }
                is ResultState.Success -> {
                    showLoading(false)

                    val profileResponse = result.data
                    val username = profileResponse?.data?.username ?: ""
                    val email = profileResponse?.data?.email ?: ""
                    val profilePhotoUrl = profileResponse?.data?.photoProfile ?: ""

                    Log.d("ProfileFragment", "Profile photo URL: $profilePhotoUrl")
                    setProfile(username, email, profilePhotoUrl)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    // Handle error state if needed
                }
            }
        })
    }

    private fun setProfile(username: String?, email: String?, profilePhotoUrl: String) {
        binding.tvUsername.text = username
        binding.tvEmail.text = email
        Glide.with(this)
            .load(profilePhotoUrl)
            .placeholder(R.drawable.img_1)
            .error(R.drawable.img_1)
            .into(binding.imgProfile)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
