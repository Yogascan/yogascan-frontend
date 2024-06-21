package com.dicoding.yogascan.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.dicoding.yogascan.api.ApiService
import com.dicoding.yogascan.data.LoginRequest
import com.dicoding.yogascan.data.ResultState
import com.dicoding.yogascan.data.SignupRequest
import com.dicoding.yogascan.data.pref.UserPreferences
import com.dicoding.yogascan.data.response.CommonUidRequestBody
import com.dicoding.yogascan.data.response.Profile
import com.dicoding.yogascan.data.response.SigninResponse
import com.dicoding.yogascan.data.response.SignupResponse
import com.dicoding.yogascan.data.response.UpdatePictureResponse
import kotlinx.coroutines.Dispatchers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File
import java.io.IOException


class UserRepository(private val apiService: ApiService, private val userPreference: UserPreferences) {

    suspend fun saveSession(user: SigninResponse) {
        userPreference.saveSession(user)
    }

    fun getSession(): LiveData<SigninResponse> {
        return userPreference.getSession().asLiveData()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun signup(username: String, email: String, password: String): LiveData<ResultState<SignupResponse>> = liveData(
        Dispatchers.IO) {
        emit(ResultState.Loading)
        try {
            val signupRequest = SignupRequest(username, email, password)
            val result = apiService.signup(signupRequest)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }
    fun login(email: String, password: String): LiveData<ResultState<SigninResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val loginRequest = LoginRequest(email, password)
            val result = apiService.login(loginRequest)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }
    fun getProfile(uid: String): LiveData<ResultState<Profile>> = liveData {
        emit(ResultState.Loading)
        try {
            val commonRequestBody = CommonUidRequestBody(uid = uid)
            val result = apiService.getProfile(commonRequestBody)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    Log.e("USERREPOSITORY", e.message.toString())
                    emit(ResultState.Error("An unknown error occurred"))
                }
            }
        }
    }

    fun updateProfilePicture(image: File, uid: String) : LiveData<ResultState<UpdatePictureResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val uidRequestBody = uid.toRequestBody("text/plain".toMediaType())
            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "profile_picture",
                image.name,
                requestImageFile
            )
            val result = apiService.setProfilePicture(uid = uidRequestBody, image = multipartBody)
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            when (e) {
                is HttpException -> {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorMessage = extractErrorMessage(errorBody)
                    emit(ResultState.Error(errorMessage ?: "An unknown error occurred"))
                }
                is IOException -> {
                    emit(ResultState.Error("Network error. Please check your connection and try again."))
                }
                else -> {
                    Log.e("USERREPOSITORY", e.message.toString())
                    emit(ResultState.Error("An unknown error occurred"))
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
        ): UserRepository = UserRepository(apiService, userPreference)
    }
}
