package com.example.classificationsupport.data.model

import com.google.gson.annotations.SerializedName

data class OrderInfo(
    @SerializedName("orderSeq") val orderSeq: Int,
    @SerializedName("companyCode") val companyCode: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("zoneNum") val zoneNum: Int,
    @SerializedName("address1") val address1: String,
    @SerializedName("orderStatus") val orderStatus: String,
    @SerializedName("roundSeq") val roundSeq: Int,
    @SerializedName("reqSeq") val reqSeq: Int,
    @SerializedName("orderMemo") val orderMemo: String,
    @SerializedName("chuteNum") val chuteNum: Int,
    @SerializedName("compSeq") val compSeq: Int,
    @SerializedName("roundDate") val roundDate: String,
    @SerializedName("centerSeq") val centerSeq: Int,
    @SerializedName("orderInvoice") val orderInvoice: String,
    @SerializedName("orderPhone") val orderPhone: String,
    @SerializedName("orderName") val orderName: String
)
