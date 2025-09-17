package com.example.classificationsupport.data.model

import com.google.gson.annotations.SerializedName

data class RoundResponse(
    @SerializedName("result") val result: String,
    @SerializedName("resultList") val resultList: List<RoundInfo>,
    @SerializedName("totalRoundCount") val totalRoundCount: Int
)