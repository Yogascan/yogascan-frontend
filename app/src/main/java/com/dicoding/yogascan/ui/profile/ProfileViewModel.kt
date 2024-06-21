package com.dicoding.yogascan.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.data.response.Profile
import com.dicoding.yogascan.data.response.SigninResponse
import kotlinx.coroutines.launch
import java.io.File

class ProfileViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
    fun getProfile(uid: String): LiveData<ResultState<Profile>> = userRepository.getProfile(uid)

    fun getSession(): LiveData<SigninResponse> = userRepository.getSession()

    fun updateProfilePicture(image: File, uid: String) = userRepository.updateProfilePicture(image, uid)

}
