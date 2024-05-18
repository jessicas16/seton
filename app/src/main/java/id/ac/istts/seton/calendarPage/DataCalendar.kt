package id.ac.istts.seton.calendarPage

import id.ac.istts.seton.taskPage.DataTask

data class DataCalendar (
    val day: String,
    val date: MutableList<Pair<String, Boolean>>,
    val tasks: MutableList<DataTask>
)