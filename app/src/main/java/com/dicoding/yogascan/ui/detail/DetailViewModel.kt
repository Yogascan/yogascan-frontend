package com.dicoding.yogascan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.repository.PoseRepository
import kotlinx.coroutines.launch

class DetailViewModel (private val poseRepository: PoseRepository) : ViewModel(){
    private val _favoriteStatus = MutableLiveData<Boolean>()
    val favoriteStatus: LiveData<Boolean> = _favoriteStatus

    fun getDetailPose(id: String?) = poseRepository.getDetailPose(id)

    fun addFavoritePose(poseId: String) {
        viewModelScope.launch {
            try {
                poseRepository.addFavoritePose(poseId)
                _favoriteStatus.value = true
            } catch (e: Exception) {
                _favoriteStatus.value = false
            }
        }
    }
}
