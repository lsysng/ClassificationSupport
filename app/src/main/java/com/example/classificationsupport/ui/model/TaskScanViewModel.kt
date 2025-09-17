package com.example.classificationsupport.ui.model

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classificationsupport.data.model.OrderInfo
import com.example.classificationsupport.repository.UnlabeledRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import kotlin.collections.forEach

class TaskScanViewModel : ViewModel () {
    private val repository = UnlabeledRepository()
    // ViewModel에서는 이벤트만 발생
    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?> = _toastMessage

    fun clearToast() {
        _toastMessage.value = null
    }

    private val _renewList = mutableStateOf<List<String>>(emptyList())
    val renewList: State<List<String>> = _renewList

    // 입력값 상태
    var invoiceInput by mutableStateOf("")
        private set


    var renewSuccessCount = mutableStateOf(0)
    var renewFailedCount = mutableStateOf(0)
    var renewFailList = mutableStateOf<List<String>>(emptyList())

    val resultMessage = mutableStateOf<String?>(null)

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private var bluetoothLeScanner: BluetoothLeScanner? = null

    private var requestBluetoothConnectPermissionLauncher: ActivityResultLauncher<String>? = null

    fun setBluetoothPermissionLauncher(launcher: ActivityResultLauncher<String>) {
        this.requestBluetoothConnectPermissionLauncher = launcher
    }
    fun fetchRenewList(targetDate: String, reqSeq: String, invoiceList: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUnlabeledOrderRenew(targetDate, reqSeq, invoiceList)
                renewFailList.value = response.renewFailList
                renewSuccessCount.value = response.renewSuccessCount
                renewFailedCount.value = response.renewFailedCount

                val msg = when (renewFailedCount.value) {
                    0 -> ""
                    1 -> "${renewFailList.value.firstOrNull()} 는 등록에 실패 하였습니다."
                    else -> "${renewFailList.value.firstOrNull()} 외 ${renewFailedCount.value - 1} 건은 등록에 실패 하였습니다."
                }

                resultMessage.value =
                    "미분류 송장번호 ${renewSuccessCount.value}개 등록이 완료되었습니다.\n$msg"

            } catch (e: Exception) {
                resultMessage.value = "등록 중 오류가 발생했습니다."
            }
        }
    }

    fun checkScanner (isCheck : Boolean, name : String) {
        try {
            if (isCheck) {
                resultMessage.value = "${name}\n블루투스 스캐너 연동되어 있습니다."
            } else {
                resultMessage.value = "블루투스 스캐너가 연결되어 있지 않습니다."
            }

        } catch (e: Exception) {
            Log.e("TaskScanViewModel error", e.message.toString())
        }
    }

    fun startBluetoothScan(context: Context) {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager?.adapter

        if (bluetoothAdapter == null) {
            Log.e("TaskScanViewModel", "BluetoothAdapter is null")
            return
        }

        try {
            val pairedDevices = bluetoothAdapter.bondedDevices

            pairedDevices.forEach { device ->
                val sppUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val socket = device.createRfcommSocketToServiceRecord(sppUUID)
                        bluetoothAdapter.cancelDiscovery()
                        socket.connect()

                        val name = device.name ?: "Unknown"

                        if (socket.isConnected) {
                            Log.d("TaskScanViewModel", "✅ 연결됨: $name")

                            if (name in listOf("SN1914610521", "SN2504410018", "SN2504310469", "SN2504310473", "SN2504310474")) {
                                checkScanner(true, name)
                                receiveData(socket)
                            }
                        }

                        socket.close()
                    } catch (e: IOException) {
                        Log.e("TaskScanViewModel", "❌ 연결 실패: ${device.name}")
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e("TaskScanViewModel", "❌ 권한 없음: ${e.message}")
        }
    }



    private fun receiveData(socket: BluetoothSocket) {
        val inputStream: InputStream = socket.inputStream
        val buffer = ByteArray(1024)
        var readCount = 0

        while (true) {
            try {
                val bytes = inputStream.read(buffer)
                if (bytes > 0) {
                    val readData = String(buffer, 0, bytes)


                    val numericOnly = readData.filter { it.isDigit() }
//                    val numericOnly = "20250909001663"
                    Log.d("TaskScanViewModel PM3", "수신 데이터: $numericOnly")

                    // UI 업데이트
                    GlobalScope.launch(Dispatchers.Main) {
                        if (_renewList.value.contains(numericOnly)) {
                            _toastMessage.value = "$numericOnly 는 이미 추가 되었습니다."
                        } else {
                            _renewList.value = _renewList.value + numericOnly
                        }
                    }

                    readCount++

                }
            } catch (e: IOException) {
                Log.e("TaskScanViewModel", "데이터 수신 오류", e)
                break
            }
        }
    }
    // 입력값 변경
    fun onInputChange(newValue: String) {
        invoiceInput = newValue
    }

    // 리스트에 추가
    fun addInvoice() {
        if (invoiceInput.isNotBlank()) {
            _renewList.value = _renewList.value + invoiceInput
            invoiceInput = "" // 입력창 초기화
        }
    }

    fun removeItemAt(invoiceValue: String) {
        _renewList.value = _renewList.value.filterNot { it == invoiceValue }
    }





}