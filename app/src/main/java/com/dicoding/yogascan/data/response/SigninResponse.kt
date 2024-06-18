package com.dicoding.yogascan.data.response

import com.google.gson.annotations.SerializedName

data class SigninResponse(

    @field:SerializedName("uid")
    val uid: String,
)
