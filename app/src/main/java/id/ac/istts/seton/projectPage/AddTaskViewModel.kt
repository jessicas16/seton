package id.ac.istts.seton.projectPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ProjectDRO
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.entity.addTaskDTO
import kotlinx.coroutines.launch

class AddTaskViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _invitedUsers = MutableLiveData<List<Users>>()
    private val _projects = MutableLiveData<ProjectDRO>()
    private val _users = MutableLiveData<List<Users>>()
    private val _checkEmail = MutableLiveData<UserDRO>()
    private var _response = MutableLiveData<BasicDRO>()

    val invitedUsers: MutableLiveData<List<Users>>
        get() = _invitedUsers

    val checkEmail: LiveData<UserDRO>
        get() = _checkEmail

    val projects: MutableLiveData<ProjectDRO>
        get() = _projects

    val users: MutableLiveData<List<Users>>
        get() = _users

    val response: LiveData<BasicDRO>
        get() = _response

    suspend fun getProjectById (projectId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getProjectById(projectId)
                Log.i("DATA_PROJECTS", res.data.toString())
                _projects.value = res
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _projects.value = ProjectDRO(
                    status = "500",
                    message = "an error occurred",
                    data = Projects(id = 0,name = "",description = "",start = "",deadline = "",pm_email = "",status = -1)
                )
            }
        }
    }

    suspend fun getProjectMembers (projectId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getProjectMembers(projectId)
                Log.i("Project Member", res.data.toString())
                _users.value = res.data
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _users.value = listOf()
            }
        }
    }

    fun checkList(email: String):Boolean{
        if(_invitedUsers.value?.size == null){
            return false
        } else {
            for (i in _invitedUsers.value!!){
                if (i.email == email){
                    return true
                }
            }
            return false
        }
    }

    suspend fun checkEmailUser(email : String = "") {
        val check = checkList(email)
        if(!check){
            viewModelScope.launch {
                try {
                    val res = repo.checkEmail(email = email)
                    Log.d("RES", res.toString())
                    if (res.status == "200") {
                        val data = res.data
                        val list = _invitedUsers.value?.toMutableList() ?: mutableListOf()
                        list.add(data)
                        Log.i("DATA_USER", list.toString())
                        _invitedUsers.value = list
                    }
                    _checkEmail.postValue(res)
                    Log.i("DATA_USER", res.data.toString())
                } catch (e: Exception) {
                    Log.e("ERROR", e.message.toString())
                    val res = UserDRO(
                        status = "500",
                        message = "An error occurred! Please try again later.",
                        data = Users("", "", null, "", null, -1)
                    )
                    _checkEmail.postValue(res)
                }
            }
        } else {
            Log.e("ERROR", "User have been invited")
            val res =  UserDRO(
                status = "400",
                message = "This user have been invited ",
                data = Users("", "", null, "", null, -1)
            )
            _checkEmail.postValue(res)
        }
    }

    fun removeUser(email: String){
        val list = _invitedUsers.value?.toMutableList() ?: mutableListOf()
        for (i in list){
            if (i.email == email){
                list.remove(i)
                break
            }
        }
        _invitedUsers.postValue(list)
    }

    fun createTask(task : addTaskDTO){
        viewModelScope.launch {
            try {
                val res = repo.createTask(task)
                _response.postValue(res)
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                val res =  BasicDRO(
                    status = "500",
                    message = "An error occurred! Please try again later.",
                    data = ""
                )
                _response.postValue(res)
            }
        }
    }
}