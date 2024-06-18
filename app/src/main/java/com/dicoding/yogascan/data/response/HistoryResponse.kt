package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(

	@field:SerializedName("history")
	val history: List<HistoryItem>
)

data class HistoryItem(

	@field:SerializedName("date")
	val date: String,

	@field:SerializedName("result")
	val result: Any,

	@field:SerializedName("pose")
	val pose: PoseHistory
)

data class PoseHistory(

	@field:SerializedName("pose_description")
	val poseDescription: String,

	@field:SerializedName("pose_image")
	val poseImage: String,

	@field:SerializedName("pose_id")
	val poseId: String,

	@field:SerializedName("pose_name")
	val poseName: String
)
