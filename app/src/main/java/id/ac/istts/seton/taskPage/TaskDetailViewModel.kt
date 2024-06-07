package id.ac.istts.seton.taskPage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.Users
import kotlinx.coroutines.launch

class TaskDetailViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _task = MutableLiveData<TaskDRO>()

    val task: MutableLiveData<TaskDRO>
        get() = _task

    fun getTaskById (taskId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getTaskDetail(taskId)
                Log.i("DATA_TASK", res.data.toString())
                _task.value = res
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _task.value =TaskDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data =  DataTask(
                        id = -1,
                        title = "",
                        deadline = "",
                        description = "",
                        priority = 0,
                        status = 0,
                        pic = Users(email = "",name = "",profile_picture = "",password = "",auth_token = "",status = 0),
                        project = Projects (id = -1, name = "", description = "", start = "", deadline = "", pm_email = "", status = 0),
                        teams = listOf(),
                        comments = listOf(),
                        attachments = listOf(),
                        checklists = listOf(),
                        labels = listOf()
                    )
                )
            }
        }
    }
}