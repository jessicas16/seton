package id.ac.istts.seton.taskPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import kotlinx.coroutines.launch

class TaskViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _tasks = MutableLiveData<List<Pair<String, List<DataTask>>>>()

    val tasks: LiveData<List<Pair<String, List<DataTask>>>>
        get() = _tasks

    fun getUserTasks(
        email : String
    ) {
        viewModelScope.launch {
            try {
                val res = repo.getUserTasks(email = email)

                Log.i("LALALALLALALLA", res.data.toString())
                val filteredTasks = listOf(
                    Pair("Upcoming", res.data.filter { it.status == 0 }),
                    Pair("Ongoing", res.data.filter { it.status == 1 }),
                    Pair("Submitted", res.data.filter { it.status == 2 }),
                    Pair("Revision", res.data.filter { it.status == 3 }),
                    Pair("Completed", res.data.filter { it.status == 4 })
                )
                Log.i("LILILILILILI", filteredTasks.toString())
                _tasks.value = filteredTasks
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _tasks.value = emptyList()
            }
        }
    }
}