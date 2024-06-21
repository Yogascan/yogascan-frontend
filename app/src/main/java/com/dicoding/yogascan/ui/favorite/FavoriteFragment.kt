package com.dicoding.yogascan.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.adapter.PoseAdapter
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    private val poseAdapter = PoseAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = poseAdapter
        }
    }

    private fun observeViewModel() {
        favoriteViewModel.getSession().observe(viewLifecycleOwner) { userData ->
            fetchFavorites(userData.uid)
        }
    }

    private fun fetchFavorites(uid: String) {
        favoriteViewModel.getFavorites(uid).observe(viewLifecycleOwner) { favoritesData ->
            when (favoritesData) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    if(favoritesData.data.favorites.isEmpty()){
                        Toast.makeText(requireContext(), "You have no favorite pose",
                            Toast.LENGTH_SHORT).show()
                    }
                    poseAdapter.submitList(favoritesData.data.favorites)
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showError(favoritesData.message)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.getSession().observe(viewLifecycleOwner) { userData ->
            fetchFavorites(userData.uid)
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
