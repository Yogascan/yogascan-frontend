package com.dicoding.yogascan.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.data.response.HistoryItem
import com.dicoding.yogascan.data.response.HistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(private val poseRepository: PoseRepository) : ViewModel() {

    private val _historyLiveData = MutableLiveData<ResultState<HistoryResponse>>()

    val historyLiveData: LiveData<ResultState<HistoryResponse>>
        get() = _historyLiveData

    fun getHistory(uid: String?) {
        viewModelScope.launch {
            try {
                val result = poseRepository.getHistory(uid)
//                _historyLiveData.value = result
            } catch (e: Exception) {
                // Handle error if needed
            }
        }
    }
}