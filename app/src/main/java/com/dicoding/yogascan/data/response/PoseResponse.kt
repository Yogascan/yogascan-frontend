package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class PoseResponse(

	@field:SerializedName("poses")
	val poses: List<PosesItem>
)

data class PosesPredictionPostData(
	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("pose_id")
	val poseId: String,

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("result")
	val result: Any,
)

data class PosesItem(

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
