package com.dicoding.yogascan.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.response.SigninResponse

class DetailViewModel (private val poseRepository: PoseRepository) : ViewModel(){
    private val _favoriteStatus = MutableLiveData<Boolean>()

    fun getDetailPose(id: String?) = poseRepository.getDetailPose(id)

    fun getSession() : LiveData<SigninResponse> = poseRepository.getSession()

    fun getUserFavorites(userId: String) = poseRepository.getFavorite(userId)

    fun deleteUserFavorite(userId: String, poseId: String) = poseRepository.deleteFavoritePose(userId, poseId)

    fun addFavoritePose(userId:String, poseId: String) = poseRepository.addFavoritePose(userId, poseId)
}
