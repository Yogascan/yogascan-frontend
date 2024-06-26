package com.dicoding.yogascan.ui.history

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.yogascan.ViewModelFactory
import com.dicoding.yogascan.adapter.HistoryAdapter
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val historyViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val historyAdapter = HistoryAdapter()
        binding.rvItemPose.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        historyViewModel.getSession().observe(viewLifecycleOwner){ user ->
            Log.d("History", user.uid)
            observeViewModel(user.uid)
        }
    }

    private fun observeViewModel(uid: String) {
        historyViewModel.getHistory(uid).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is ResultState.Loading -> showLoading(true)
                is ResultState.Success -> {
                    showLoading(false)
                    result.data.let { historyResponse ->
                        val historyList = historyResponse.history
                        (binding.rvItemPose.adapter as? HistoryAdapter)?.submitList(historyList)
                    }
                }
                is ResultState.Error -> {
                    showLoading(false)
                    showError(result.message?: "Unknown error")
                }
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
