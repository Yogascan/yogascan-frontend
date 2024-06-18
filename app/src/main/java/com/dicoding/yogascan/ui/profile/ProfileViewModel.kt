package com.dicoding.yogascan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.data.response.ProfileResponse
import com.dicoding.yogascan.data.response.SigninResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun getProfile(uid: String): LiveData<ResultState<ProfileResponse>> {
        return userRepository.getProfile(uid)
    }

    fun getSession(): LiveData<SigninResponse> {
        return userRepository.getSession().asLiveData()
    }
}
