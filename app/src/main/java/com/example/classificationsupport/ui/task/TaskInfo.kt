package com.example.classificationsupport.ui.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.classificationsupport.R
import com.example.classificationsupport.ui.model.TaskInfoViewModel
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskInfo (navController: NavHostController, viewModel: TaskInfoViewModel = viewModel ()) {
    var dateInput by remember { mutableStateOf(LocalDate.now()) }

    val totalRoundCount by viewModel.totalRoundCount

    // 화면이 처음 로딩될 때 API 호출
    LaunchedEffect(Unit) {
        viewModel.fetchRoundList(dateInput.toString())
    }

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),

        content = { padding ->
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color.White)
            ) {

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(Color(0xFF2742B2))
                        .padding(all = 10.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.topbar_logo),
                        contentDescription = null,
                        modifier = Modifier
                            .size(200.dp)
                    )
                }

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(all = 10.dp),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$totalRoundCount 건의 배차 정보가 있습니다.",
                        color = Color.Black,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))


                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 왼쪽 화살표
                    Image(
                        painter = painterResource(id = R.drawable.arrow_left_main),
                        contentDescription = "왼쪽 화살표",
                        modifier = Modifier
                            .size(height = 24.dp, width = 24.dp)
                            .clickable {
                                dateInput = dateInput.minusDays(1)
                                viewModel.fetchRoundList(dateInput.toString())
                            }
                    )

                    // 날짜 텍스트
                    Text(
                        text = "$dateInput",
                        color = Color.Black,
                        modifier = Modifier.padding(10.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    // 오른쪽 화살표
                    Image(
                        painter = painterResource(id = R.drawable.arrow_right_main),
                        contentDescription = "오른쪽 화살표",
                        modifier = Modifier
                            .size(height = 24.dp, width = 24.dp)
                            .clickable {
                                dateInput = dateInput.plusDays(1)
                                viewModel.fetchRoundList(dateInput.toString())
                            }
                    )
                }

                if (viewModel.roundList.value.isNullOrEmpty()) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .padding(all = 10.dp),

                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("등록된 정보가 없습니다.")
                    }
                } else {
                    LazyColumn {
                        items(viewModel.roundList.value ?: emptyList()) { round ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(10.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                            ) {
                                // 카드 내부 콘텐츠

                                Column(modifier = Modifier.padding(5.dp)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(70.dp)
                                            .padding(all = 10.dp),

                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.main_truck_icon),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(height = 40.dp, width = 40.dp)
                                                .padding(end = 5.dp)
                                                .clickable {
                                                    navController.navigate("detail/${round.reqSeq}/${round.roundDate}/${round.rowNum}")
                                                }
                                        )
                                        Column {
                                            Text(
                                                text = "${round.rowNum} 회차 배차",
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                modifier = Modifier.padding()
                                            )
                                            Text(
                                                text = "${round.roundDateTime}",
                                                color = Color.Black,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding()
                                            )
                                        }


                                        Spacer(modifier = Modifier.weight(1f))

                                        Image(
                                            painter = painterResource(id = R.drawable.img_scan),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .padding(end = 5.dp)
                                                .clickable {
                                                    navController.navigate("scan/${round.reqSeq}/${round.roundDate}")
                                                }
                                        )

                                        Text(
                                            text = "미분류 스캔 등록",
                                            color = Color.Black,
                                            fontSize = 15.sp,
                                            modifier = Modifier
                                                .clickable {
                                                    navController.navigate("scan/${round.reqSeq}/${round.roundDate}")
                                                }
                                        )


                                    }
                                    // ✅ 첫 번째 행
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(60.dp)
                                                .background(
                                                    color = Color(0xFFF5F5F5),
                                                    shape = RoundedCornerShape(15.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${round.reqSeq}",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(60.dp)
                                                .background(
                                                    color = Color(0xFFF5F5F5),
                                                    shape = RoundedCornerShape(15.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${round.chuteCount} 슈트",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // ✅ 두 번째 행
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(60.dp)
                                                .background(
                                                    color = Color(0xFFF5F5F5),
                                                    shape = RoundedCornerShape(15.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${round.vehicleCount} 차량",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(60.dp)
                                                .background(
                                                    color = Color(0xFFF5F5F5),
                                                    shape = RoundedCornerShape(15.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${round.totalOrderCount} 배송지",
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }


                                }
                            }
                        }
                    }
                }

            }
        }
    )


}
