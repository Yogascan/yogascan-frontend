package com.dicoding.yogascan.ui.scan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.repository.PoseRepository
import com.dicoding.yogascan.data.response.PosesPredictionPostData
import com.dicoding.yogascan.data.response.PredictionResponse
import com.dicoding.yogascan.data.response.SigninResponse
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDateTime

class ScanViewModel(private val poseRepository: PoseRepository) : ViewModel() {
    private val _predictionResult = MutableLiveData<ResultState<PredictionResponse>>()
    val predictionResult: LiveData<ResultState<PredictionResponse>> get() = _predictionResult

    fun getSession() : LiveData<SigninResponse> {
        return poseRepository.getSession()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun postPrediction(imgFile: File, uid: String, poseId: String) {
        _predictionResult.value = ResultState.Loading

        viewModelScope.launch {
            try {
                val requestImageFile = imgFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData("image", imgFile.name, requestImageFile)
                val result = poseRepository.postPrediction(multipartBody)
                val predictionData = PosesPredictionPostData(
                    uid = uid,
                    poseId = poseId,
                    result = result.data.confidence,
                    date = LocalDateTime.now().toString()
                )
                _predictionResult.value = ResultState.Success(result)
                poseRepository.savePrediction(predictionData)
            } catch (e: Exception) {
                _predictionResult.value = ResultState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
