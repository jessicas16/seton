package com.example.seton.calendarPage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.seton.CalendarFont
import com.example.seton.R
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CalendarActivity : ComponentActivity() {
    private val vm: CalendarViewModel by viewModels<CalendarViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalendarPreview()
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun CalendarPreview() {
        val listCalendar by vm.listCalendar.observeAsState(emptyList())
        val calendar by vm.cal.observeAsState(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))
        val selected by vm.selected.observeAsState(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))

        ConstraintLayout(
            Modifier
                .fillMaxSize()
                .background(Color.White)) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp)) {
                Row {
                    Image(
                        painter = painterResource(R.drawable.prev_icon),
                        contentDescription = "previous",
                        modifier = Modifier.weight(1f).clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            vm.updateCalendar(-1)
                        }
                    )
                    Text(
                        text = "${calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)} ${calendar.get(Calendar.YEAR)}",
                        fontFamily = CalendarFont.fontBold,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .weight(1f),
                        textAlign = TextAlign.Center
                    )
                    Image(
                        painter = painterResource(R.drawable.next_icon),
                        contentDescription = "next",
                        modifier = Modifier.weight(1f).clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            vm.updateCalendar(1)
                        }
                    )
                }
                Row(Modifier.fillMaxWidth()) {
                    for (cal in listCalendar) {
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row {
                                Text(
                                    text = cal.day,
                                    fontFamily = CalendarFont.fontBold,
                                    fontSize = 10.sp,
                                    color = Color(0xFF626262),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                            for (date in cal.date) {
                                Row {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(45.dp)
                                            .padding(4.dp)
                                            .background(
                                                if (
                                                    date.second &&
                                                    calendar.get(Calendar.MONTH).toString() == selected.get(Calendar.MONTH).toString() &&
                                                    calendar.get(Calendar.YEAR).toString() == selected.get(Calendar.YEAR).toString() &&
                                                    date.first == selected.get(Calendar.DATE).toString()
                                                ) Color(0xFF0E9794)
                                                else if (cal.day == "SAT" || cal.day == "SUN") Color(0xFFD8FDFF)
                                                else Color.White,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable(
                                                indication = null,
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                if (date.second) vm.changeSelected(date.first.toInt(), true)
                                                else vm.changeSelected(date.first.toInt(), false)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            Row(
                                                Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth()) {
                                                Text(
                                                    text = date.first,
                                                    fontFamily = CalendarFont.fontBold,
                                                    fontSize = 12.sp,
                                                    color = Color.Black,
                                                    textAlign = TextAlign.Center,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .align(Alignment.Bottom)
                                                        .alpha(
                                                            if (!date.second) 0.5f
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
                                                for (i in 0..< cal.tasks.size.coerceAtMost(2)) {
                                                    if (cal.tasks[i].deadline.split("T")[0].split("-")[2] == date.first) {
                                                        Spacer(modifier = Modifier.padding(1.dp))
                                                        Box(
                                                            modifier = Modifier
                                                                .size(6.dp)
                                                                .background(
                                                                    Color(
                                                                        when (cal.tasks[i].status) {
                                                                            0 -> 0xFFFFDD60
                                                                            1 -> 0xFFF4976C
                                                                            2 -> 0xFF87CBCA
                                                                            3 -> 0xFFFACBB6
                                                                            else -> 0xFF2DA4A2
                                                                        }
                                                                    ).copy(
                                                                        if (!date.second) 0.5f
                                                                        else 1f
                                                                    ),
                                                                    RoundedCornerShape(6.dp)
                                                                )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.padding(2.dp))
                            }
                        }
                        if (cal.day != "SUN") {
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
}