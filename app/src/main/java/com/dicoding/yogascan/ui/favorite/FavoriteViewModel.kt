package com.dicoding.yogascan.ui.favorite

import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.repository.PoseRepository

class FavoriteViewModel(private val poseRepository: PoseRepository) : ViewModel() {
    fun getSession() = poseRepository.getSession()
    fun getFavorites(userId : String) = poseRepository.getFavorite(userId)
}
