package com.example.seton.calendarPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.config.ApiConfiguration
import com.example.seton.taskPage.DataTask
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class CalendarViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _tasks = MutableLiveData<List<Pair<String, List<DataTask>>>>()
    private val _listCalendar = MutableLiveData<List<DataCalendar>>()

    val tasks: LiveData<List<Pair<String, List<DataTask>>>>
        get() = _tasks

    val listCalendar: MutableLiveData<List<DataCalendar>>
        get() = _listCalendar

    init {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"))
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        _listCalendar.value = listOf(
            DataCalendar("MON", mutableListOf()),
            DataCalendar("TUE", mutableListOf()),
            DataCalendar("WED", mutableListOf()),
            DataCalendar("THU", mutableListOf()),
            DataCalendar("FRI", mutableListOf()),
            DataCalendar("SAT", mutableListOf()),
            DataCalendar("SUN", mutableListOf())
        )
        val dayFirstDate = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)?.substring(0, 3)?.uppercase()
        val dayIndex = when(dayFirstDate) {
            "MON" -> 0
            "TUE" -> 1
            "WED" -> 2
            "THU" -> 3
            "FRI" -> 4
            "SAT" -> 5
            else -> 6
        }
        val maxDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        calendar.add(Calendar.DAY_OF_MONTH, -1)
        var fillDate = 1
        var fillPrevDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - dayIndex + 1
        for (i in 0..< dayIndex) {
            _listCalendar.value!![i].date.add(Pair(fillPrevDate.toString(), false))
            fillPrevDate++
        }
        for (i in dayIndex..6) {
            _listCalendar.value!![i].date.add(Pair(fillDate.toString(), true))
            fillDate++
        }
        while (fillDate < maxDate) {
            for (i in 0..6) {
                _listCalendar.value!![i].date.add(Pair(fillDate.toString(), true))
                fillDate++
                if (fillDate > maxDate) {
                    fillDate = 1
                    for (j in (i + 1)..6) {
                        _listCalendar.value!![j].date.add(Pair(fillDate.toString(), false))
                        fillDate++
                    }
                    fillDate = 32
                    break
                }
            }
        }
    }

    fun getUserTasks() {
        viewModelScope.launch {
            try {
                val res = repo.getUserTasks()
                Log.i("DATA_TASK", res.data.toString())
                val filteredTasks = listOf(
                    Pair("Upcoming", res.data.filter { it.status == 0 }),
                    Pair("Ongoing", res.data.filter { it.status == 1 }),
                    Pair("Submitted", res.data.filter { it.status == 2 }),
                    Pair("Revision", res.data.filter { it.status == 3 }),
                    Pair("Completed", res.data.filter { it.status == 4 })
                )
                _tasks.value = filteredTasks
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _tasks.value = emptyList()
            }
        }
    }
}