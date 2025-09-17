package com.example.classificationsupport.data.model

import com.google.gson.annotations.SerializedName

data class OrderResponse(
    @SerializedName("result") val result: String,
    @SerializedName("resultList") val resultList: List<OrderInfo>
)

