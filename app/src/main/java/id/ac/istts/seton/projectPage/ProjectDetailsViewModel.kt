package id.ac.istts.seton.projectPage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ac.istts.seton.config.ApiConfiguration
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.taskPage.DataTask
import kotlinx.coroutines.launch

class ProjectDetailsViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _projects = MutableLiveData<ProjectDetailDRO>()
    private val _invitedUsers = MutableLiveData<List<Users>>()
    private val _checkEmail = MutableLiveData<UserDRO>()
    private val _response = MutableLiveData<BasicDRO>()
    private val _tasks = MutableLiveData<List<Pair<String, List<DataTask>>>>()

    val invitedUsers: MutableLiveData<List<Users>>
        get() = _invitedUsers
    val checkEmail: LiveData<UserDRO>
        get() = _checkEmail

    val projects: MutableLiveData<ProjectDetailDRO>
        get() = _projects

    val response: LiveData<BasicDRO>
        get() = _response

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

    fun getProjectById (projectId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getProjectDetail(projectId)
                Log.i("DATA_PROJECTS", res.data.toString())
                _projects.value = res
                _invitedUsers.value = res.data.members
            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                _projects.value = ProjectDetailDRO(
                    status = "500",
                    message = "an error occurred",
                    data = DetailProject(
                        projectName = "",
                        projectDescription = "",
                        projectStart = "",
                        projectDeadline = "",
                        projectManager = Users(email = "",name = "",profile_picture = "",password = "",auth_token = "",status = 0),
                        members = listOf(),
                        tasks = listOf(),
                        upcomingTask = 0,
                        ongoingTask = 0,
                        submittedTask = 0,
                        revisionTask = 0,
                        completedTask = 0,
                        projectStatus = ""
                    )
                )
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

    suspend fun checkEmailUser(projectId: String, email : String = "") {
        val check = checkList(email)
        if(!check){
            viewModelScope.launch {
                try {
                    val res = repo.checkEmail(email = email)
                    Log.d("RES", res.toString())
                    if (res.status == "200") {
                        //post to db
                        val res2 = repo.addMemberProject(projectId = projectId, email = email)
                        Log.d("RES2", res2.toString())

                        //add to list
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

    fun removeMember(projectId: String, email: String){
        viewModelScope.launch {
            try{
                val res = repo.deleteMemberProject(projectId = projectId, email = email)
                Log.d("RES", res.toString())
                if (res.status == "200") {
                    val list = _invitedUsers.value?.toMutableList() ?: mutableListOf()
                    for (i in list){
                        if (i.email == email){
                            list.remove(i)
                            break
                        }
                    }
                    _invitedUsers.postValue(list)
                }
                _response.postValue(res)
            } catch (e: Exception){
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