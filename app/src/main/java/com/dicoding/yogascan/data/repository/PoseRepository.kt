package com.dicoding.yogascan.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.dicoding.yogascan.api.ApiService
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.pref.UserPreferences
import com.dicoding.yogascan.data.response.CommonRequestBody
import com.dicoding.yogascan.data.response.CommonFavoriteResponseBody
import com.dicoding.yogascan.data.response.DetailResponse
import com.dicoding.yogascan.data.response.FavoriteResponse
import com.dicoding.yogascan.data.response.CommonUidRequestBody
import com.dicoding.yogascan.data.response.HistoryRequest
import com.dicoding.yogascan.data.response.HistoryResponse
import com.dicoding.yogascan.data.response.PoseResponse
import com.dicoding.yogascan.data.response.PosesPredictionPostData
import com.dicoding.yogascan.data.response.PredictionResponse
import com.dicoding.yogascan.data.response.SigninResponse
import okhttp3.MultipartBody
import java.io.IOException
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

class PoseRepository private constructor(private val apiService: ApiService, private val userPreference: UserPreferences) {

    fun getSession(): LiveData<SigninResponse> {
        return userPreference.getSession().asLiveData()
    }

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

    fun addFavoritePose(userId : String, poseId: String): LiveData<ResultState<CommonFavoriteResponseBody>> = liveData {
            emit(ResultState.Loading)
            try {
                val addFavoritesItem = CommonRequestBody(
                    userId, poseId
                )
                val response = apiService.postFavorite(addFavoritesItem)
                emit(ResultState.Success(CommonFavoriteResponseBody(response.message)))
            } catch (e: Exception) {
                emit(ResultState.Error("An error occurred: ${e.message}"))
            }
    }

    fun deleteFavoritePose(userId: String, poseId: String) : LiveData<ResultState<CommonFavoriteResponseBody>> = liveData {
        emit(ResultState.Loading)
        try {
            val deleteFavoriteItem = CommonRequestBody(
                userId, poseId
            )
            val response = apiService.deleteFavoritePoses(deleteFavoriteItem)
            emit(ResultState.Success(CommonFavoriteResponseBody(response.message)))
        } catch(e : Exception) {
            emit(ResultState.Error("An error occurred: ${e.message}"))
        }
    }


    fun getFavorite(uid: String) : LiveData<ResultState<FavoriteResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val getFavorite = CommonUidRequestBody(uid = uid)
            val result = apiService.getFavoritePoses(getFavorite)
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

    suspend fun savePrediction(predictionPostData: PosesPredictionPostData){
        try {
            apiService.saveHistory(predictionPostData)
        } catch (e: Exception){
            throw e
        }
    }

    fun getHistory(uid: String?): LiveData<ResultState<HistoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val userUid = HistoryRequest(uid!!)
            val result = apiService.getHistory(userUid)
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
            userPreference: UserPreferences
        ): PoseRepository = PoseRepository(apiService, userPreference)

    }
}
