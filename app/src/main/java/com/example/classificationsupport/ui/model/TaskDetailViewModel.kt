package com.example.classificationsupport.ui.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classificationsupport.data.model.OrderInfo
import com.example.classificationsupport.repository.UnlabeledRepository
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel () {
    private val repository = UnlabeledRepository()
    private val _orderList = mutableStateOf<List<OrderInfo>>(emptyList())
    val orderList: State<List<OrderInfo>> = _orderList

    fun fetchOrderList(targetDate: String, reqSeq: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUnlabeledList(targetDate, reqSeq)
                _orderList.value = response.resultList
            } catch (e: Exception) {
                // 예외 처리
            }
        }
    }




}