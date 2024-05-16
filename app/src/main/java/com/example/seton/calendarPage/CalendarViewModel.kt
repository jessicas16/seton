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
    private val _tasks = MutableLiveData<List<DataTask>>()
    private val _listCalendar = MutableLiveData<List<DataCalendar>>()
    private val _cal = MutableLiveData(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))
    private val _selected = MutableLiveData(Calendar.getInstance(TimeZone.getTimeZone("GMT+7")))

    val listCalendar: MutableLiveData<List<DataCalendar>>
        get() = _listCalendar

    val cal: MutableLiveData<Calendar>
        get() = _cal

    val selected: MutableLiveData<Calendar>
        get() = _selected

    init {
        viewModelScope.launch {
            getUserTasks()
            updateCalendar(0)
        }
    }

    fun updateCalendar(move: Int) {
        val calendar = _cal.value!!.clone() as Calendar
        calendar.add(Calendar.MONTH, move)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        _cal.value = calendar.clone() as Calendar

        _listCalendar.value = listOf(
            DataCalendar("MON", mutableListOf(), mutableListOf()),
            DataCalendar("TUE", mutableListOf(), mutableListOf()),
            DataCalendar("WED", mutableListOf(), mutableListOf()),
            DataCalendar("THU", mutableListOf(), mutableListOf()),
            DataCalendar("FRI", mutableListOf(), mutableListOf()),
            DataCalendar("SAT", mutableListOf(), mutableListOf()),
            DataCalendar("SUN", mutableListOf(), mutableListOf())
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
                for (task in _tasks.value!!) {
                    val deadline = task.deadline.split("T")[0]
                    val date = deadline.split("-")[2]
                    val month = deadline.split("-")[1]
                    val year = deadline.split("-")[0]
                    if (
                        date.toInt().toString() == fillDate.toString() &&
                        month.toInt().toString() == (_cal.value!!.get(Calendar.MONTH) + 1).toString() &&
                        year.toInt().toString() == _cal.value!!.get(Calendar.YEAR).toString()
                    ) {
                        Log.i("DEADLINE", task.deadline)
                        _listCalendar.value!![i].tasks.add(task)
                    }
                }
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

        changeSelected(
            if (_selected.value!!.get(Calendar.DATE) > _cal.value!!.getActualMaximum(Calendar.DAY_OF_MONTH))
                _cal.value!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            else
                _selected.value!!.get(Calendar.DATE),
            true
        )
    }

    fun changeSelected(value: Int, isActive: Boolean) {
        val newValue = cal.value!!.clone() as Calendar
        if (!isActive) {
            newValue.add(Calendar.MONTH, if (value < 8) 1 else -1)
            updateCalendar(if (value < 8) 1 else -1)
        }
        newValue.set(Calendar.DATE, value)
        _selected.value = newValue.clone() as Calendar
    }

    private suspend fun getUserTasks() {
        try {
            val res = repo.getUserTasks()
            _tasks.value = res.data
        } catch (e: Exception) {
            Log.e("ERROR", e.message.toString())
            _tasks.value = emptyList()
        }
    }
}