package com.dicoding.yogascan.api

import com.dicoding.yogascan.data.LoginRequest
import com.dicoding.yogascan.data.SignupRequest
import com.dicoding.yogascan.data.response.DetailResponse
import com.dicoding.yogascan.data.response.FavoriteResponse
import com.dicoding.yogascan.data.response.FavoritesItem
import com.dicoding.yogascan.data.response.HistoryItem
import com.dicoding.yogascan.data.response.HistoryResponse
import com.dicoding.yogascan.data.response.PoseResponse
import com.dicoding.yogascan.data.response.PredictionResponse
import com.dicoding.yogascan.data.response.ProfileResponse
import com.dicoding.yogascan.data.response.SigninResponse
import com.dicoding.yogascan.data.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
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

    @POST("saveHistory")
    suspend fun saveHistory(@Body historyItem: HistoryItem)

    @GET("history")
    suspend fun getHistory(
        @Path("uid") uid: String?
    ): HistoryResponse

    @Multipart
    @POST("uploadImage")
    suspend fun saveImageToServer(
        @Part image: MultipartBody.Part,
        @Part("prediction") prediction: RequestBody,
        @Part("confidence") confidence: RequestBody
    ):HistoryResponse

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): SigninResponse

    @POST("signup")
    suspend fun signup(
        @Body request: SignupRequest
    ): SignupResponse

//    @FormUrlEncoded
//    @POST("login")
//    suspend fun login(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): SigninResponse
//
//
//    @FormUrlEncoded
//    @POST("signup")
//    suspend fun signup(
//        @Field("username")username: String,
//        @Field("email") email: String,
//        @Field("password") password: String,
//    ): SignupResponse

    @POST("user")
    fun getProfile(
        @Field("uid") uid: String,
    ): ProfileResponse

    @POST("favorite")
    suspend fun postFavorite(
        @Body favoritesItem: FavoritesItem
    ): FavoriteResponse

    @GET("favorite")
    suspend fun getFavoritePoses(
    ): FavoriteResponse
}