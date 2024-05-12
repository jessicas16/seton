package com.example.seton.projectPage

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seton.config.ApiConfiguration
import com.example.seton.entity.ProjectDRO
import com.example.seton.entity.ProjectDetailDRO
import com.example.seton.entity.Projects
import com.example.seton.entity.Users
import com.squareup.moshi.Json
import kotlinx.coroutines.launch

class ProjectDetailsViewModel: ViewModel() {
    private var repo = ApiConfiguration.defaultRepo
    private val _projects = MutableLiveData<ProjectDetailDRO>()

    val projects: MutableLiveData<ProjectDetailDRO>
        get() = _projects

    fun getProjectById (projectId: String) {
        viewModelScope.launch {
            try {
                val res = repo.getProjectDetail(projectId)
                Log.i("DATA_PROJECTS", res.data.toString())
                _projects.value = res
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
}