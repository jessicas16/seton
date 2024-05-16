package com.example.seton.config

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.seton.config.local.AppDatabase
import com.example.seton.entity.*
import com.example.seton.projectPage.DataProject
import com.example.seton.projectPage.DetailProject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.time.LocalDate

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

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun checkRemember(): String{
        var user: String = ""
        withContext(Dispatchers.IO){
            val remember = dataSourceLocal.rememberDao().get()
            if(remember != null){
                val expired = LocalDate.parse(remember.expired)
                if(LocalDate.now().isAfter(expired)){
                    dataSourceLocal.rememberDao().clearDb()
                }else{
                    user = remember.email
                }
            }
        }
        return user
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginUser(userLoginDTO:userLoginDTO):BasicDRO {
        var loginUser: BasicDRO? = null

        try {
            loginUser = dataSourceRemote.loginUser(userLoginDTO)
        }catch (e: Exception){

        }

        withContext(Dispatchers.IO){
            if(loginUser != null){
                val expired = LocalDate.now().plusDays(30).toString()
                val remember = Remember(
                    email = userLoginDTO.email,
                    expired = expired
                )

                dataSourceLocal.rememberDao().clearDb()
                dataSourceLocal.rememberDao().insert(remember)
            }


        }

        return loginUser!!
    }

    suspend fun logoutUser(){
        withContext(Dispatchers.IO){
            dataSourceLocal.rememberDao().clearDb()
        }
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
                                if(dataSourceLocal.projectMemberDao().checkByProjectIdAndEmail(data.id, member.email) == null){
                                    val projectMember = ProjectMembers(
                                        project_id = data.id,
                                        member_email = member.email
                                    )
                                    dataSourceLocal.projectMemberDao().insert(projectMember)
                                }

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
        var createProject: BasicDRO? = null

        try {
            createProject = dataSourceRemote.createProject(projectDTO)

            val project = Projects(
                id = createProject.data.toString().toInt(),
                name = projectDTO.name,
                description = projectDTO.description,
                start = projectDTO.startTime,
                deadline = projectDTO.deadline,
                pm_email = projectDTO.pm_email,
                status = 0
            )

            withContext(Dispatchers.IO){
                dataSourceLocal.projectDao().insert(project)

                if(projectDTO.members_email != null){
                    for(member in projectDTO.members_email){
                        val m = ProjectMembers(
                            project_id = createProject.data.toString().toInt(),
                            member_email = member
                        )
                        dataSourceLocal.projectMemberDao().insert(m)
                    }
                }
            }


        }catch (e: Exception){

        }

        return createProject!!
    }

    suspend fun getProjectById(projectId: String): ProjectDRO {
        var projectDRO: ProjectDRO?

        try {
            projectDRO = dataSourceRemote.getProjectById(projectId)
            if(projectDRO.status != "404"){
                withContext(Dispatchers.IO){
                    dataSourceLocal.projectDao().update(projectDRO!!.data)
                }
            }
        }catch (e: Exception){
            withContext(Dispatchers.IO){
                val getProjectFromLocal = dataSourceLocal.projectDao().getById(projectId.toInt())
                if(getProjectFromLocal != null){
                    projectDRO = ProjectDRO(
                        status = "200",
                        message = "Success get project by id from local!",
                        data = getProjectFromLocal
                    )
                }else{
                    val project = Projects(
                        id = -1,
                        name = "",
                        description = "",
                        start = "",
                        deadline = "",
                        pm_email = "",
                        status = -1
                    )

                    projectDRO = ProjectDRO(
                        status = "404",
                        message = "Project not found!",
                        data = project
                    )
                }
            }
        }

        return projectDRO!!
    }

    suspend fun getProjectDetail(projectId: String): ProjectDetailDRO {
        var projectDetailDRO: ProjectDetailDRO? = null
        var message = "Success get project by id from local!"

        try {
            val getProjectDetailFromApi = dataSourceRemote.getProjectDetail(projectId)
            val data = getProjectDetailFromApi.data

            val project = Projects(
                id = projectId.toInt(),
                name = data.projectName,
                description = data.projectDescription,
                start = data.projectStart,
                deadline = data.projectDeadline,
                pm_email = data.projectManager.email,
                status = if(data.projectStatus == "Ongoing") 0 else 1
            )

            withContext(Dispatchers.IO){
                try {
                    dataSourceLocal.projectDao().update(project)
                }catch (e: Exception){}

                try {
                    dataSourceLocal.userDao().update(data.projectManager)
                }catch (e: Exception){}
            }

            for(member in data.members){

                withContext(Dispatchers.IO){
                    try {
                        if(dataSourceLocal.projectMemberDao().checkByProjectIdAndEmail(projectId.toInt(), member.email) == null){
                            val projectMember = ProjectMembers(
                                project_id = projectId.toInt(),
                                member_email = member.email
                            )
                            dataSourceLocal.projectMemberDao().insert(projectMember)
                        }

                    }catch (e: Exception){}
                }
            }

            for(task in data.tasks){
                withContext(Dispatchers.IO){
                    try {
                        if(dataSourceLocal.taskDao().getById(task.id) == null){
                            dataSourceLocal.taskDao().insert(task)
                        }else{
                            dataSourceLocal.taskDao().update(task)
                        }
                    }catch (e: Exception){}
                }
            }

            message = "Success get project by id from API!"
        }catch (e: Exception){}

        val project = withContext(Dispatchers.IO){dataSourceLocal.projectDao().getById(projectId.toInt())}
        val projectManager = withContext(Dispatchers.IO){dataSourceLocal.userDao().getByEmail(project!!.pm_email)}
        val projectMembers = withContext(Dispatchers.IO){dataSourceLocal.projectMemberDao().getUserByProjectId(projectId.toInt())}
        val projectTasks = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByProjectId(projectId.toInt()).toMutableList()}

        projectTasks.sortWith(compareBy<Tasks> { it.status }.thenBy { it.title })

        var upcomingTask = 0
        var ongoingTask = 0
        var submittedTask = 0
        var revisionTask = 0
        var completedTask = 0

        for(t in projectTasks){
            if (t.status == 0) {
                upcomingTask++
            } else if (t.status == 1) {
                ongoingTask++
            } else if (t.status == 2) {
                submittedTask++
            } else if (t.status == 3) {
                revisionTask++
            } else if (t.status == 4) {
                completedTask++
            }
        }

        val detailProject = DetailProject(
            projectName = project!!.name,
            projectDescription = project.description,
            projectStart = project.start,
            projectDeadline = project.deadline,
            projectManager = projectManager,
            members = projectMembers,
            tasks = projectTasks,
            upcomingTask = upcomingTask,
            ongoingTask = ongoingTask,
            submittedTask = submittedTask,
            revisionTask = revisionTask,
            completedTask = completedTask,
            projectStatus = if(project.status == 0) "Ongoing" else "Completed"
        )

        projectDetailDRO = ProjectDetailDRO(
            status = "200",
            message = message,
            data = detailProject
        )


        return projectDetailDRO!!
    }

    //TASKS
    suspend fun getUserTasks(force:Boolean = false, email: String = "ivan.s21@mhs.istts.ac.id"): ListTaskDRO {
        return dataSourceRemote.getUserTasks(email)
    }

    suspend fun getProjectMembers(projectId: String): ListUserDRO {
        var message = "Success get project by id from local!"
        try {
            val getMemberFromApi = withContext(Dispatchers.IO){dataSourceRemote.getProjectMembers(projectId)}
            val data = getMemberFromApi.data

            for(member in data){

                withContext(Dispatchers.IO){
                    try {
                        if(dataSourceLocal.projectMemberDao().checkByProjectIdAndEmail(projectId.toInt(), member.email) == null){
                            val projectMember = ProjectMembers(
                                project_id = projectId.toInt(),
                                member_email = member.email
                            )
                            dataSourceLocal.projectMemberDao().insert(projectMember)
                        }

                    }catch (e: Exception){}
                }
            }
        }catch (e: Exception){}

        var getMemberFromLocal = withContext(Dispatchers.IO){dataSourceLocal.projectMemberDao().getUserByProjectId(projectId.toInt())}

        val listUserDRO = ListUserDRO(
            status = "200",
            message = message,
            data = getMemberFromLocal
        )

        return listUserDRO
    }

    suspend fun createTask(taskDTO: addTaskDTO): BasicDRO {
        return dataSourceRemote.createTask(taskDTO)
    }
}