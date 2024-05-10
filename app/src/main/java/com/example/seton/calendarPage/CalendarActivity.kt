package com.example.seton.calendarPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.AppFont

class CalendarActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarPreview()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
//    val userTasks by vm.tasks.observeAsState(emptyList())
//    LaunchedEffect(key1 = Unit) {
//        vm.getUserTasks()
//    }
    ConstraintLayout(Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Row {
                // Bulan dan Tahun
            }
            Row(Modifier.fillMaxWidth()) {
                // Calendar
                Column(
                    modifier = Modifier.weight(1f).padding(start = 4.dp, end = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Senin
                    Row {
                        Text(
                            text = "SENIN",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                    Row {
                        Button(
                            onClick = {

                            },
                            colors = ButtonColors(Color.White, Color.Black, Color.LightGray, Color.Gray),
                            shape = RoundedCornerShape(size = 8.dp)
                        ) {
                            Text(
                                text = "1",
                                fontFamily = AppFont.fontBold,
                                fontSize = 10.sp,
                                color = Color.Black
                            )
                        }
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Selasa
                    Row {
                        Text(
                            text = "SELASA",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Rabu
                    Row {
                        Text(
                            text = "RABU",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Kamis
                    Row {
                        Text(
                            text = "KAMIS",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Jumat
                    Row {
                        Text(
                            text = "JUMAT",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(horizontal = 2.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Sabtu
                    Row {
                        Text(
                            text = "SABTU",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
                Column(
                    modifier = Modifier.weight(1f).padding(start = 2.dp, end = 4.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    // Minggu
                    Row {
                        Text(
                            text = "MINGGU",
                            fontFamily = AppFont.fontBold,
                            fontSize = 10.sp,
                            color = Color(0xFF626262)
                        )
                    }
                }
            }
            Row {
                // Detail
            }
        }
    }
}