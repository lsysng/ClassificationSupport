package com.example.classificationsupport.data.model

import com.google.gson.annotations.SerializedName

data class RenewResultResponse(
    @SerializedName("result") val result: String,
    @SerializedName("renewSuccessCount") val renewSuccessCount: Int,
    @SerializedName("renewFailedCount") val renewFailedCount: Int,
    @SerializedName("renewFailList") val renewFailList: List<String>
)
