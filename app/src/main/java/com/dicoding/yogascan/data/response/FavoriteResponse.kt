package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class FavoritesItem(

	@field:SerializedName("pose_description")
	val poseDescription: String,

	@field:SerializedName("pose_benefits")
	val poseBenefits: String,

	@field:SerializedName("pose_image")
	val poseImage: String,

	@field:SerializedName("pose_name")
	val poseName: String,

	@field:SerializedName("pose_id")
	val poseId: String
)


data class FavoriteResponse(

	@field:SerializedName("favorites")
	val favorites: List<PosesItem>
)

data class CommonRequestBody(
	@field:SerializedName("uid")
	val userId:String,

	@field:SerializedName("pose_id")
	val poseId: String
)

data class CommonFavoriteResponseBody(
	@field:SerializedName("message")
	val message:String
)

data class CommonUidRequestBody(
	@field:SerializedName("uid")
	val uid: String
)
