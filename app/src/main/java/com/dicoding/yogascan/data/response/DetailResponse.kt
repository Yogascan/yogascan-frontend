package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class DetailResponse(

	@field:SerializedName("pose")
	val pose: Pose
)

data class Pose(

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
