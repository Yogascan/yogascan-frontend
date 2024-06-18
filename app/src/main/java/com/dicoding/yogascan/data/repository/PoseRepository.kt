package com.dicoding.yogascan.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.yogascan.api.ApiService
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.response.DetailResponse
import com.dicoding.yogascan.data.response.FavoriteResponse
import com.dicoding.yogascan.data.response.FavoritesItem
import com.dicoding.yogascan.data.response.HistoryItem
import com.dicoding.yogascan.data.response.HistoryResponse
import com.dicoding.yogascan.data.response.PoseResponse
import com.dicoding.yogascan.data.response.PredictionResponse
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class PoseRepository private constructor(private val apiService: ApiService) {
    fun getPoses(uid: String?): LiveData<ResultState<PoseResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getPoses(uid)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occured"))
                }

                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }

                else -> {
                    emit(ResultState.Error("An unknown error occured"))
                }
            }
        }
    }

    fun getDetailPose(id: String?): LiveData<ResultState<DetailResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getDetailPose(id)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occured"))
                }

                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }

                else -> {
                    emit(ResultState.Error("An unknown error occured"))
                }
            }
        }
    }

    suspend fun postPrediction(image: MultipartBody.Part): PredictionResponse {
        try {
            return apiService.postPrediction(image)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun addFavoritePose(poseId: String): LiveData<ResultState<FavoriteResponse>> {
        return liveData {
            emit(ResultState.Loading)
            try {
                val favoritesItem = FavoritesItem(
                    poseId = poseId,
                    poseDescription = "",
                    poseBenefits = "",
                    poseImage = "",
                    poseName = ""
                )
                apiService.postFavorite(favoritesItem)
                emit(ResultState.Success(FavoriteResponse(listOf(favoritesItem))))
            } catch (e: Exception) {
                emit(ResultState.Error("An error occurred: ${e.message}"))
            }
        }
    }

    suspend fun saveHistory(historyItem: HistoryItem) {
        try {
            apiService.saveHistory(historyItem)
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getHistory(uid: String?): LiveData<ResultState<HistoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val result = apiService.getHistory(uid)
            emitSource(MutableLiveData(ResultState.Success(result)))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occured"))
                }

                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }

                else -> {
                    emit(ResultState.Error("An unknown error occured"))
                }
            }
        }
    }
    private fun extractErrorMessage(errorBody: String?): String? {
        return try {
            val json = JSONObject(errorBody)
            json.getString("message")
        } catch (e: JSONException) {
            null
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
        ): PoseRepository = PoseRepository(apiService)

    }
}