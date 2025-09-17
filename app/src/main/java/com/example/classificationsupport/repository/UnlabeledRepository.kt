package com.example.classificationsupport.repository

import com.example.classificationsupport.data.model.OrderResponse
import com.example.classificationsupport.data.model.RenewResultResponse
import com.example.classificationsupport.data.model.RoundResponse
import com.example.classificationsupport.data.network.RetrofitInstance

class UnlabeledRepository {
    suspend fun getDateRoundList(targetDate: String): RoundResponse {
        return RetrofitInstance.api.getDateRoundList(targetDate)
    }

    suspend fun getUnlabeledList(targetDate: String, reqSeq: String): OrderResponse {
        return RetrofitInstance.api.getUnlabeledList(targetDate, reqSeq)
    }

    suspend fun getUnlabeledOrderRenew(roundDate: String, reqSeq: String, invoiceList: String): RenewResultResponse {
        return RetrofitInstance.api.getUnlabeledOrderRenew(roundDate, reqSeq, invoiceList)
    }

}