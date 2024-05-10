package com.example.seton.calendarPage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.CalendarFont

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
    ConstraintLayout(Modifier.fillMaxSize().background(Color.White)) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)) {
            Row {
                // Bulan dan Tahun
                Text(
                    text = "May 2023",
                    fontFamily = CalendarFont.fontBold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Row(Modifier.fillMaxWidth()) {
                // Calendar
                val listCalendar = listOf(
                    DataCalendar("SENIN", listOf("1", "8", "15", "22", "29")),
                    DataCalendar("SELASA", listOf("2", "9", "16", "23", "30")),
                    DataCalendar("RABU", listOf("3", "10", "17", "24", "31")),
                    DataCalendar("KAMIS", listOf("4", "11", "18", "25", "1")),
                    DataCalendar("JUMAT", listOf("5", "12", "19", "26", "2")),
                    DataCalendar("SABTU", listOf("6", "13", "20", "27", "3")),
                    DataCalendar("MINGGU", listOf("7", "14", "21", "28", "4")),
                )
                for (calendar in listCalendar) {
                    Column(
                        modifier = Modifier
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row {
                            Text(
                                text = calendar.day,
                                fontFamily = CalendarFont.fontBold,
                                fontSize = 10.sp,
                                color = Color(0xFF626262),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        for (date in calendar.date) {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(45.dp)
                                        .padding(4.dp)
                                        .background(
                                            if (calendar.day == "SABTU" || calendar.day == "MINGGU") Color(
                                                0xFFD8FDFF
                                            ) else Color.White,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable {
                                            // Button onclick
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column {
                                        Row(
                                            Modifier
                                                .weight(1f)
                                                .fillMaxWidth()) {
                                            Text(
                                                text = date,
                                                fontFamily = CalendarFont.fontBold,
                                                fontSize = 12.sp,
                                                color = Color.Black,
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .align(Alignment.Bottom)
                                                    .alpha(
                                                        if (date == "1" || date == "2" || date == "3" || date == "4") 0.5f
                                                        else 1f
                                                    )
                                            )
                                        }
                                        Row(
                                            Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .padding(top = 3.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            for (i in 1..2) {
                                                Spacer(modifier = Modifier.padding(1.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .size(6.dp)
                                                        .background(
                                                            Color(0xFFF4976C),
                                                            RoundedCornerShape(6.dp)
                                                        )
                                                        .alpha(
                                                            if (date == "1" || date == "2" || date == "3" || date == "4") 0.5f
                                                            else 1f
                                                        )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.padding(2.dp))
                        }
                    }
                    if (calendar.day != "MINGGU") {
                        Spacer(modifier = Modifier.padding(2.dp))
                    }
                }
            }
            Row {
                // Detail
            }
        }
    }
}