package com.example.seton.calendarPage

import com.example.seton.taskPage.DataTask

data class DataCalendar (
    val day: String,
    val date: MutableList<Pair<String, Boolean>>,
    val tasks: MutableList<DataTask>
)