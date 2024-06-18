package com.dicoding.yogascan.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.response.PredictionResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ScanViewModel(private val poseRepository: PoseRepository) : ViewModel() {
    private val _predictionResult = MutableLiveData<ResultState<PredictionResponse>>()
    val predictionResult: LiveData<ResultState<PredictionResponse>> get() = _predictionResult

    fun postPrediction(imgFile: File) {
        _predictionResult.value = ResultState.Loading

        viewModelScope.launch {
            try {
                // Convert File to MultipartBody.Part
                val requestImageFile = imgFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData("image", imgFile.name, requestImageFile)

                // Send image to repository for prediction
                val result = poseRepository.postPrediction(multipartBody)
                // Set the prediction result to Success state
                _predictionResult.value = ResultState.Success(result)
            } catch (e: Exception) {
                // Handle errors by setting the result to Error state
                _predictionResult.value = ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }
}