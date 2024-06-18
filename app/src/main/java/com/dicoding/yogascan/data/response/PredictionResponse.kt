package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class PredictionResponse(

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: Status
)

data class Status(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("message")
	val message: String
)

data class Data(

	@field:SerializedName("confidence")
	val confidence: Double,

	@field:SerializedName("prediction")
	val prediction: String,

	@field:SerializedName("pose_image")
	val poseImage: String,
)
