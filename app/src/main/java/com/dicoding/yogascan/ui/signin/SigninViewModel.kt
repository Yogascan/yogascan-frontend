package com.dicoding.yogascan.ui.signin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.UserRepository
import com.dicoding.yogascan.data.response.SigninResponse
import kotlinx.coroutines.launch

class SigninViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableLiveData<ResultState<SigninResponse>>()
    val loginResult: LiveData<ResultState<SigninResponse>> = _loginResult

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginResult.value = ResultState.Loading
            try {
                repository.login(email, password).observeForever { result ->
                    when (result) {
                        is ResultState.Success -> {
                            _loginResult.value = ResultState.Success(result.data)
                            viewModelScope.launch {
                                repository.saveSession(result.data)
                            }
                        }
                        is ResultState.Error -> {
                            _error.value = result.message
                            _loginResult.value = ResultState.Error(result.message)
                        }
                        is ResultState.Loading -> {
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An error occurred"
                _loginResult.value = ResultState.Error(e.message ?: "An error occurred")
            }
        }
    }
}
