package com.dicoding.yogascan.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.response.FavoriteResponse
import com.dicoding.yogascan.data.repository.PoseRepository

class FavoriteViewModel(private val poseRepository: PoseRepository) : ViewModel() {

    private var _addFavoriteStatus: LiveData<ResultState<FavoriteResponse>>? = null

//    fun addFavoritePose(poseId: String) {
//        _addFavoriteStatus = poseRepository.addFavoritePose(poseId)
//    }

    fun getAddFavoriteStatus(): LiveData<ResultState<FavoriteResponse>>? {
        return _addFavoriteStatus
    }
}