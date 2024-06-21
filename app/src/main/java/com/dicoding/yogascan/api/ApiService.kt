package com.dicoding.yogascan.api

import com.dicoding.yogascan.data.LoginRequest
import com.dicoding.yogascan.data.SignupRequest
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
import com.dicoding.yogascan.data.response.Profile
import com.dicoding.yogascan.data.response.SigninResponse
import com.dicoding.yogascan.data.response.SignupResponse
import com.dicoding.yogascan.data.response.UpdatePictureResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("poses")
    suspend fun getPoses(
        @Query("uid") uid: String?
    ): PoseResponse

    @GET("pose/{id}")
    suspend fun getDetailPose(
        @Path("id") id: String?
    ): DetailResponse

    @Multipart
    @POST("prediction")
    suspend fun postPrediction(
        @Part image: MultipartBody.Part,
    ): PredictionResponse

    @POST("history")
    suspend fun saveHistory(
        @Body predictionPostData: PosesPredictionPostData)
    
    @Headers("Content-Type: application/json")
    @POST("histories")
    suspend fun getHistory(
        @Body request: HistoryRequest
    ): HistoryResponse
    
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): SigninResponse

    @POST("signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): SignupResponse

    @POST("user")
    suspend fun getProfile(
        @Body uid: CommonUidRequestBody,
    ): Profile

    @POST("favorite")
    suspend fun postFavorite(
        @Body favoritesItem: CommonRequestBody
    ): CommonFavoriteResponseBody

    @HTTP(method = "DELETE", path = "favorite", hasBody = true)
    suspend fun deleteFavoritePoses(
        @Body favoritesItem: CommonRequestBody
    ) : CommonFavoriteResponseBody

    @POST("favorites")
    suspend fun getFavoritePoses(
        @Body getFavorite: CommonUidRequestBody
    ): FavoriteResponse

    @Multipart
    @POST("update-picture")
    suspend fun setProfilePicture(
        @Part("uid") uid: RequestBody,
        @Part image: MultipartBody.Part
    ) : UpdatePictureResponse
}
