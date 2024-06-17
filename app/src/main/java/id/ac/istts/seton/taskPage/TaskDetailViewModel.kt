package id.ac.istts.seton.taskPage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.Users
import kotlinx.coroutines.launch

class TaskDetailViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _task = MutableLiveData<TaskDRO>()
    private val _status = MutableLiveData<BasicDRO>()

    val task: MutableLiveData<TaskDRO>
        get() = _task

    val status: MutableLiveData<BasicDRO>
        get() = _status

    fun getTaskById (taskId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getTaskDetail(taskId)
                Log.i("LALALALLALALLA", res.data.toString())
                Log.i("LILILILILILI", res.data.statusTask.toString())
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
                        priority = -1,
                        statusTask = -1,
                        pic = Users(
                            email = "",
                            name = "",
                            profile_picture = "",
                            password = "",
                            auth_token = "",
                            status = -1
                        ),
                        project = Projects(
                            id = -1,
                            name = "",
                            description = "",
                            start = "",
                            deadline = "",
                            pm_email = "",
                            status = -1
                        ),
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

    fun updateTaskStatus(
        taskId: String,
        status: String
    ){
        viewModelScope.launch {
            try {
                val res = repo.updateTaskStatus(taskId, status)
                _status.value = res
                getTaskById(taskId)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _status.value = BasicDRO(
                    status = "500",
                    message = "Internal Server Error",
                    data = null
                )
            }
        }
    }
}