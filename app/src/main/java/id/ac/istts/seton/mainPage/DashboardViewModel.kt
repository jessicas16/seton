package id.ac.istts.seton.mainPage

import android.icu.util.Calendar
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class DashboardViewModel: ViewModel(){
    private var repo = ApiConfiguration.defaultRepo
    private val _tasks = MutableLiveData<List<Pair<String, List<DataTaskDashboard>>>>()
    val tasks: LiveData<List<Pair<String, List<DataTaskDashboard>>>>
        get() = _tasks
    private val _numCount = MutableLiveData<ArrayList<Float>>()
    val numCount: LiveData<ArrayList<Float>>
        get() = _numCount

    private var maxData = 0


    fun getUserTasksDashboard(email: String) {
        viewModelScope.launch {
            try {
                val res = repo.getUserTasksDashboard(email = email)
                Log.i("DATA_TASK", res.data.toString())
                val filteredTasks = listOf(
                    Pair("Upcoming", res.data.filter { it.status == 0 }),
                    Pair("Ongoing", res.data.filter { it.status == 1 }),
                    Pair("Submitted", res.data.filter { it.status == 2 }),
                    Pair("Revision", res.data.filter { it.status == 3 }),
                    Pair("Completed", res.data.filter { it.status == 4 })
                )
                _tasks.value = filteredTasks
                maxData = 0
                val numCountList = ArrayList<Float>()
                for (i in 0..6) {
                    var dt = Date()
                    val c: Calendar = Calendar.getInstance()
                    c.setTime(dt)
                    c.add(Calendar.DATE, i)
                    dt = c.getTime()
                    val sdf2 = SimpleDateFormat("yyyy-MM-dd")
                    val currentDate2 = sdf2.format(dt)
                    var temp = 0
                    for (task in res.data) {
                        println("cetak task deadline = " + task.deadline.substring(0, 10) + " --- " + currentDate2)
                        if (task.deadline.substring(0, 10) == currentDate2 && task.status == 0) {
                            temp += 1
                        }
                        else if (task.deadline.substring(0, 10) == currentDate2 && task.status == 1){
                            temp += 1
                        }
                        else if (task.deadline.substring(0,10) == currentDate2 && task.status == 2){
                            temp += 1
                        }
                    }
                    numCountList.add(temp.toFloat())
                    if(maxData < temp.toFloat()){
                        maxData = temp
                    }
//                    numCountList.add(temp.toFloat() * i)
//                    if(maxData < temp.toFloat() * i){
//                        maxData = temp * i
//                    }
                }
                println("cetak " +numCountList)
                if(maxData > 0){
                    for (i in 0..6){
                        numCountList.set(i, numCountList.get(i) / maxData)
                    }
                }
                numCountList.add((maxData.toFloat() * 2).toFloat())
                println("cetak akhir " +numCountList)

                _numCount.value = numCountList

            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _tasks.value = emptyList()
            }
        }
    }
}