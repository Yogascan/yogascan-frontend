package com.dicoding.yogascan.ui.home

<<<<<<< HEAD
import android.os.Bundle
=======
import android.content.Intent
import android.os.Bundle
import android.util.Log
>>>>>>> c80c871 (commit)
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
<<<<<<< HEAD
=======
import androidx.appcompat.widget.Toolbar
>>>>>>> c80c871 (commit)
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.adapter.PoseAdapter
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.FragmentHomeBinding
<<<<<<< HEAD
=======
import com.dicoding.yogascan.ui.detail.DetailActivity
import com.dicoding.yogascan.ui.detail.DetailActivity.Companion.KEY
>>>>>>> c80c871 (commit)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
<<<<<<< HEAD
    private val username: String by lazy {
        arguments?.getString("username") ?: ""
    }
=======

>>>>>>> c80c871 (commit)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

<<<<<<< HEAD
        binding.tvuname.text = "WELCOME"
=======
>>>>>>> c80c871 (commit)
        val poseAdapter = PoseAdapter()
        binding.rvItemPose.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = poseAdapter
        }
        val poseId = "A3WvDIrObgYhlWm0znaN"
        homeViewModel.getPoses(poseId).observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
<<<<<<< HEAD
=======
                    Log.d("HomeFragment", "Data from API: ${result.data}")
>>>>>>> c80c871 (commit)
                    poseAdapter.submitList(result.data.poses) // Extract the poses list from PoseResponse
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showError(result.message)
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}