package com.example.seton.config

import com.example.seton.entity.*
import org.json.JSONObject

class DefaultRepo(
    private val dataSourceRemote : ApiService,
) {
    //TEST CONNECTION
    suspend fun checkConnection():BasicDRO {
        return dataSourceRemote.checkConnection()
    }

    //USER
    suspend fun getAllUser(force:Boolean = false):List<Users> {
        return dataSourceRemote.getAllUser()
    }

    suspend fun getAllUserExceptLoginUser(force:Boolean = false, email: String): ListUserDRO{
        return dataSourceRemote.getAllUserExceptLoginUser(email)
    }

    suspend fun checkEmail(email: String): UserDRO {
        return dataSourceRemote.checkEmail(email)
    }

    suspend fun registerUser(userDTO : userDTO): BasicDRO {
        return dataSourceRemote.registerUser(userDTO)
    }

    suspend fun loginUser(userLoginDTO:userLoginDTO):BasicDRO {
        return dataSourceRemote.loginUser(userLoginDTO)
    }

    //PROJECTS
    suspend fun getUserProjects(force:Boolean = false, email: String = "ivan.s21@mhs.istts.ac.id"): ListProjectDRO {
        return dataSourceRemote.getUserProjects(email)
    }

    suspend fun createProject(projectDTO: addProjectDTO): BasicDRO {
        return dataSourceRemote.createProject(projectDTO)
    }

    suspend fun getProjectById(projectId: String): ProjectDRO {
        return dataSourceRemote.getProjectById(projectId)
    }

    suspend fun getProjectDetail(projectId: String): ProjectDetailDRO {
        return dataSourceRemote.getProjectDetail(projectId)
    }

    //TASKS
    suspend fun getUserTasks(force:Boolean = false, email: String = "ivan.s21@mhs.istts.ac.id"): ListTaskDRO {
        return dataSourceRemote.getUserTasks(email)
    }

    suspend fun getProjectMembers(projectId: String): ListUserDRO {
        return dataSourceRemote.getProjectMembers(projectId)
    }

    suspend fun createTask(taskDTO: addTaskDTO): BasicDRO {
        return dataSourceRemote.createTask(taskDTO)
    }
}