package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("profile")
	val data: Profile? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Profile(

	@field:SerializedName("profile_picture")
	val photoProfile: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,
)

data class UpdatePictureResponse(
	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("profile_picture")
	val pictureUrl : String,
)
