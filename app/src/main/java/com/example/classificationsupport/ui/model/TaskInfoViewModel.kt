package com.example.classificationsupport.ui.model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classificationsupport.data.model.RoundInfo
import com.example.classificationsupport.repository.UnlabeledRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.State


class TaskInfoViewModel : ViewModel () {
    private val repository = UnlabeledRepository()

    private val _roundList = mutableStateOf<List<RoundInfo>>(emptyList())
    val roundList: State<List<RoundInfo>> = _roundList

    private val _totalRoundCount = mutableStateOf(0)
    val totalRoundCount: State<Int> = _totalRoundCount


    fun fetchRoundList(targetDate: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDateRoundList(targetDate)
                _roundList.value = response.resultList
                _totalRoundCount.value = response.totalRoundCount
            } catch (e: Exception) {
                // 예외 처리
            }
        }
    }



}