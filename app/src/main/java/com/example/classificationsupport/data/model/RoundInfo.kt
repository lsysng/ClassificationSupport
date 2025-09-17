package com.example.classificationsupport.data.model

import com.google.gson.annotations.SerializedName

data class RoundInfo(
    @SerializedName("chuteCount") val chuteCount: Int,
    @SerializedName("vehicleCount") val vehicleCount: Int,
    @SerializedName("rowNum") val rowNum: Int,
    @SerializedName("roundDate") val roundDate: String,
    @SerializedName("reqSeq") val reqSeq: Int,
    @SerializedName("roundDateTime") val roundDateTime: String,
    @SerializedName("totalOrderCount") val totalOrderCount: Int
)