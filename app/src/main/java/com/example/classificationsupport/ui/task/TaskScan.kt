package com.example.classificationsupport.ui.task

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.camera.core.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classificationsupport.R
import com.example.classificationsupport.RequestCameraPermission
import com.example.classificationsupport.ui.components.CustomAlertDialogModel
import com.example.classificationsupport.ui.model.TaskScanViewModel
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScan(navController: NavController, seq: String?, date: String?, viewModel: TaskScanViewModel = viewModel()) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            viewModel.startBluetoothScan(context)
        } else {
            Toast.makeText(context, "블루투스 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
        }
    }


    var permissionGranted by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.resultMessage.value) {
        viewModel.resultMessage.value?.let { msg ->
            CustomAlertDialogModel.showDialog("", msg, onConfirm = {
                viewModel.resultMessage.value = null // 다이얼로그 닫은 후 초기화
            })
        }
    }

    val toastMessage by viewModel.toastMessage.observeAsState()

    // 메시지가 있으면 Toast 띄우기
    toastMessage?.let { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.clearToast() // 메시지 초기화
    }

    // 화면이 처음 로딩될 때 API 호출
    LaunchedEffect(Unit) {
        val requiredPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requiredPermissions.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        val missingPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }

        if (missingPermissions.isNotEmpty()) {
            permissionLauncher.launch(missingPermissions.toTypedArray())
        } else {
            viewModel.startBluetoothScan(context)
        }

    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    CustomAlertDialogModel.showDialog("", "등록된 미분류 송장번호  등록 하시겠습니까?", onConfirm = {
                        viewModel.fetchRenewList(date.toString(), seq.toString(), viewModel.renewList.value.joinToString(","))
                    }, onCancel = {})
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2742B2))
                    .padding(10.dp)
                    .navigationBarsPadding(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2742B2))
            ) {
                Text("등록요청", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(it)
                    .navigationBarsPadding()
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text("미분류 스캔 등록", color = Color.White)
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(R.drawable.white_arrow),
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.Black
                    )
                )

                Row(modifier = Modifier
                    .height(150.dp)
                    .fillMaxWidth()
                    .clipToBounds()
                ) {
                    if (permissionGranted) {
                        CameraPreview(modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp))
                    } else {
                        RequestCameraPermission {
                            permissionGranted = true
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color.White),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("등록수량 : ", fontSize = 15.sp, color = Color(0xFF858E96))
                    Text("${viewModel.renewList.value.size}건", fontSize = 15.sp, color = Color.Black)
                }

                Box(modifier = Modifier.fillMaxWidth()) {
                    TextField(
                        value = viewModel.invoiceInput,
                        onValueChange = { viewModel.onInputChange(it) },
                        placeholder = { Text("송장번호 입력", color = Color(0xFF858E96)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, shape = RoundedCornerShape(15.dp))
                            .padding(15.dp),
                        colors = TextFieldDefaults.colors( // Changed from textFieldColors
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            disabledTextColor = Color.Gray, // Example: Add other states as needed
                            cursorColor = Color.White, // Example
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent, // Example

                        )
                    )
                    IconButton(
                        onClick = { viewModel.addInvoice() },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 20.dp)
                    ) {
                        Icon(painterResource(R.drawable.scanner_search_icon), contentDescription = null, modifier = Modifier.size(24.dp))
                    }
                }

                Row(modifier = Modifier.padding(15.dp)) {
                    Text("번호", color = Color(0xFF858E96), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Text("송장번호", color = Color(0xFF858E96), modifier = Modifier.weight(6f), textAlign = TextAlign.Center)
                    Text("관리", color = Color(0xFF858E96), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
                Divider(color = Color(0xFF858E96), thickness = 1.dp, modifier = Modifier.padding(horizontal = 15.dp))

                val renewList = viewModel.renewList.value
                val listState = rememberLazyListState()

                // 리스트가 갱신될 때마다 가장 마지막 항목으로 스크롤
                // reverseLayout = true일 경우 0번이 최신 항목
                LaunchedEffect(renewList.size) {
                    Log.d("TaskScan", "LaunchedEffect(renewList.size) {")
                    snapshotFlow { renewList.size }
                        .collectLatest {
                            Log.d("TaskScan", "Scrolling to top: $it")
                            listState.animateScrollToItem(it)
                        }
                }

                LazyColumn(
                    reverseLayout = true,
                    state = listState,
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Top // 항목을 위쪽에 정렬

                ) {
                    itemsIndexed(viewModel.renewList.value.reversed() ?: emptyList()) { index, item ->
                        Row(modifier = Modifier.padding(15.dp)) {
                            Text("${index + 1}", color = Color(0xFF858E96), modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                            Text("$item", color = Color(0xFF858E96), modifier = Modifier.weight(6f), textAlign = TextAlign.Center)
                            Text(
                                "삭제"
                                , color = Color(0xFF858E96)
                                , modifier = Modifier
                                    .weight(1f)
                                    .clickable{
                                        CustomAlertDialogModel.showDialog("", "$item 를 삭제 하시겠습니까?", onConfirm = {
                                            viewModel.removeItemAt(item)
                                        }, onCancel = {})
                                    }
                                , textAlign = TextAlign.Center
                            )
                        }

                    }
                }
            }
        },

    )
}


@Composable
fun CameraPreview(modifier: Modifier = Modifier) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(previewView.context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner, // ✅ 여기서 LocalLifecycleOwner 사용
                    cameraSelector,
                    preview
                )
            }, ContextCompat.getMainExecutor(previewView.context))
        }
    )
}

