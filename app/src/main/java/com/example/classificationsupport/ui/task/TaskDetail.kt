package com.example.classificationsupport.ui.task

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.classificationsupport.R
import com.example.classificationsupport.ui.model.TaskDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetail(navController: NavController, seq: String?, date: String?, num: String?, viewModel: TaskDetailViewModel = viewModel()) {

    // 화면이 처음 로딩될 때 API 호출
    LaunchedEffect(Unit) {
        viewModel.fetchOrderList(date.toString(), seq.toString())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text("$date  $num 회차 배차", color = Color.White)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.White)
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("미분류 스캔 등록 : ", fontSize = 15.sp, color = Color(0xFF858E96))
            Text("${viewModel.orderList.value.size}건", fontSize = 15.sp, color = Color.Black)
        }


        if (viewModel.orderList.value.isNullOrEmpty()) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(all = 10.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("등록된 정보가 없습니다.", color = Color.White)
            }
        } else {
            LazyColumn {
                items(viewModel.orderList.value ?: emptyList()) { order ->
                    Row(modifier = Modifier.padding(15.dp)) {
                        Text(
                            "${order.orderSeq}",
                            color = Color(0xFF858E96),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            "${order.orderInvoice}",
                            color = Color(0xFF858E96),
                            modifier = Modifier.weight(4f),
                            textAlign = TextAlign.Left
                        )
                        Text(
                            "${order.chuteNum}번 슈트",
                            color = Color(0xFF858E96),
                            modifier = Modifier.weight(3f),
                            textAlign = TextAlign.Left
                        )
                    }
                    Divider(
                        color = Color(0xFF858E96),
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                }
            }
        }
    }
}