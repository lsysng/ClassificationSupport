package com.example.classificationsupport.data.network

import com.example.classificationsupport.data.model.OrderResponse
import com.example.classificationsupport.data.model.RenewResultResponse
import com.example.classificationsupport.data.model.RoundResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/app/unlabeled/dateRoundList")
    suspend fun getDateRoundList(
        @Query("targetDate") targetDate: String
    ): RoundResponse

    @GET("/app/unlabeled/unlabeledList")
    suspend fun getUnlabeledList(
        @Query("targetDate") targetDate: String,
        @Query("reqSeq") reqSeq: String
    ): OrderResponse

    @GET("/app/unlabeled/unlabeledOrderRenew")
    suspend fun getUnlabeledOrderRenew(
        @Query("roundDate") roundDate: String,
        @Query("reqSeq") reqSeq: String,
        @Query("invoiceList") invoiceList: String
    ): RenewResultResponse


}
