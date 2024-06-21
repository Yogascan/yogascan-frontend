package com.dicoding.yogascan.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.response.SigninResponse

class HistoryViewModel(private val poseRepository: PoseRepository) : ViewModel() {
    fun getSession() : LiveData<SigninResponse> {
        return poseRepository.getSession()
    }
    fun getHistory(uid: String?)  = poseRepository.getHistory(uid)
}
