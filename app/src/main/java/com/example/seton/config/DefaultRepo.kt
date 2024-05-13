package com.example.seton.config

import com.example.seton.config.local.AppDatabase
import com.example.seton.entity.*
import com.example.seton.projectPage.DataProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DefaultRepo(
    private val dataSourceLocal: AppDatabase,
    private val dataSourceRemote : ApiService,
) {
    //TEST CONNECTION
    suspend fun checkConnection():Boolean {
        return try {
            dataSourceRemote.checkConnection()
            false
        }catch (e: Exception){
            true
        }
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
    suspend fun getUserProjects(force:Boolean = true, email: String = "ivan.s21@mhs.istts.ac.id"): ListProjectDRO {
        var status = "200"
        var message = "Success get user projects from local!"

        var dataProject: MutableList<DataProject> = mutableListOf()

        if(force){
            try {

                //FETCH API
                val getUserProjectFromApi = dataSourceRemote.getUserProjects(email)

                //CLEAR DB
                withContext(Dispatchers.IO){
                    dataSourceLocal.projectMemberDao().clearProjectMembers()
                    dataSourceLocal.userDao().clearUsers()
                    dataSourceLocal.taskDao().clearTasks()
                    dataSourceLocal.projectDao().clearProjects()
                }

                for(data in getUserProjectFromApi.data){
                    //Insert User from owner
                    withContext(Dispatchers.IO){
                        try {
                            dataSourceLocal.userDao().insert(data.owner)
                        }catch (e: Exception){

                        }

                    }

                    //Insert Users from members and insert ProjectMembers
                    for(member in data.members){
                        withContext(Dispatchers.IO){
                            try {
                                dataSourceLocal.userDao().insert(member)
                            }catch (e: Exception){

                            }

                        }

                        withContext(Dispatchers.IO){
                            try {
                                val projectMember = ProjectMembers(
                                    project_id = data.id,
                                    member_email = member.email
                                )
                                dataSourceLocal.projectMemberDao().insert(projectMember)
                            }catch (e: Exception){

                            }

                        }
                    }

                    //Insert Tasks
                    for(task in data.tasks){
                        withContext(Dispatchers.IO){
                            try {
                                dataSourceLocal.taskDao().insert(task)
                            }catch (e: Exception){

                            }

                        }
                    }

                    //Insert Project
                    val project = Projects(
                        id = data.id,
                        name = data.name,
                        description = data.description,
                        start = data.start,
                        deadline = data.deadline,
                        pm_email = data.owner.email,
                        status = data.status
                    )

                    withContext(Dispatchers.IO){
                        try {
                            dataSourceLocal.projectDao().insert(project)
                        }catch (e: Exception){

                        }

                    }

                }

                message = "Success get user projects from API!"
            }catch (e: Exception){
                status = "400"
                message = "Connection error, get user projects from local!"
            }
        }

        var getUserProjectsByOwnerFromLocal: List<Projects>
        var getUserProjectsByMemberFromLocal: List<Projects>

        withContext(Dispatchers.IO){
            getUserProjectsByOwnerFromLocal = dataSourceLocal.projectDao().getByOwner(email)
            getUserProjectsByMemberFromLocal = dataSourceLocal.projectDao().getByMember(email)
        }

        var projects: MutableList<Projects> = mutableListOf()
        projects.addAll(getUserProjectsByOwnerFromLocal)
        projects.addAll(getUserProjectsByMemberFromLocal)

        for(project in projects){
            val owner = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByEmail(project.pm_email) }
            val members = withContext(Dispatchers.IO){ dataSourceLocal.projectMemberDao().getUserByProjectId(project.id) }
            val tasks = withContext(Dispatchers.IO){ dataSourceLocal.taskDao().getByProjectId(project.id) }

            dataProject.add(DataProject(
                id = project.id,
                name = project.name,
                description = project.description,
                start = project.start,
                deadline = project.deadline,
                status = project.status,
                owner = owner,
                members = members,
                tasks = tasks
            ))


        }

        val listProject = ListProjectDRO(
            status = status,
            message = message,
            data = dataProject
        )

        return listProject
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