package com.dicoding.yogascan.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.data.response.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun signup(name: String, email: String, password: String): LiveData<ResultState<SignupResponse>> {
        return repository.signup(name, email, password)
    }
}