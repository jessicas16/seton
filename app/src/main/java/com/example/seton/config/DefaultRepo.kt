package com.example.seton.config

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.seton.config.local.AppDatabase
import com.example.seton.entity.BasicDRO
import com.example.seton.entity.ListProjectDRO
import com.example.seton.entity.ListTaskDRO
import com.example.seton.entity.ListUserDRO
import com.example.seton.entity.ProjectDRO
import com.example.seton.entity.ProjectDetailDRO
import com.example.seton.entity.ProjectMembers
import com.example.seton.entity.Projects
import com.example.seton.entity.Remember
import com.example.seton.entity.TaskTeams
import com.example.seton.entity.Tasks
import com.example.seton.entity.UserDRO
import com.example.seton.entity.Users
import com.example.seton.entity.addProjectDTO
import com.example.seton.entity.addTaskDTO
import com.example.seton.entity.userDTO
import com.example.seton.entity.userLoginDTO
import com.example.seton.projectPage.DataProject
import com.example.seton.projectPage.DetailProject
import com.example.seton.taskPage.DataTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
            val members = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByProjectId(project.id) }
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

            if(createProject.status == "201"){
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
                                project_id = createProject!!.data.toString().toInt(),
                                member_email = member
                            )
                            dataSourceLocal.projectMemberDao().insert(m)
                        }
                    }
                }

            }else{
                createProject = null
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
        val projectMembers = withContext(Dispatchers.IO){dataSourceLocal.userDao().getByProjectId(projectId.toInt())}
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


        return projectDetailDRO
    }

    suspend fun addMemberProject(projectId: String, email: String) : BasicDRO{
        return dataSourceRemote.addMemberProject(projectId, email)
    }

    suspend fun deleteMemberProject(projectId: String, email: String) : BasicDRO{
        return dataSourceRemote.deleteMemberProject(projectId, email)
    }

    //TASKS
    suspend fun getUserTasks(force:Boolean = true, email: String = "ivan.s21@mhs.istts.ac.id"): ListTaskDRO {
        var message = "Success get project by id from local!"

        if(force){
            try {
                val getUserTaskFromApi = withContext(Dispatchers.IO){dataSourceRemote.getUserTasks(email)}

                for(data in getUserTaskFromApi.data){
                    val task = Tasks(
                        id = data.id,
                        title = data.title,
                        deadline = data.deadline,
                        description = data.description,
                        priority = data.priority,
                        status = data.status,
                        pic_email = data.pic.email,
                        project_id = data.project.id
                    )

                    withContext(Dispatchers.IO){
                        try {
                            if(dataSourceLocal.taskDao().getById(data.id) == null){
                                dataSourceLocal.taskDao().insert(task)
                            }else{
                                dataSourceLocal.taskDao().update(task)
                            }

                        }catch (e: Exception){}
                    }

                    for(team in data.teams){
                        try {
                            dataSourceLocal.userDao().insert(team)
                        }catch (e: Exception){}

                        try {
                            if(dataSourceLocal.taskTeamDao().checkByTaskIdAndEmail(data.id, team.email) == null){
                                val taskTeam = TaskTeams(
                                    task_id = data.id,
                                    team_email = team.email
                                )
                                dataSourceLocal.taskTeamDao().insert(taskTeam)
                            }
                        }catch (e: Exception){}
                    }

                    for(comment in data.comments){
                        try {
                            dataSourceLocal.commentDao().insert(comment)
                        }catch (e: Exception){}
                    }

                    for(attachment in data.attachments){
                        try {
                            dataSourceLocal.attachmentDao().insert(attachment)
                        }catch (e: Exception){}
                    }

                    for(checklist in data.checklists){
                        try {
                            dataSourceLocal.checklistDao().insert(checklist)
                        }catch (e: Exception){}
                    }

                    for(label in data.labels){
                        try {
                            dataSourceLocal.labelDao().insert(label)
                        }catch (e: Exception){}
                    }
                }
                message = "Success get project by id from API!"
            }catch (e: Exception){}
        }

        var getUserTasksByPICFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByPIC(email)}
        var getUserTasksByTeamFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByTeam(email)}

        var tasks: MutableList<Tasks> = mutableListOf()
        tasks.addAll(getUserTasksByPICFromLocal)
        tasks.addAll(getUserTasksByTeamFromLocal)

        var dataTask: MutableList<DataTask> = mutableListOf()

        for(task in tasks){
            val pic = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByEmail(task.pic_email) }
            val project = withContext(Dispatchers.IO){ dataSourceLocal.projectDao().getById(task.project_id)}
            val teams = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByTaskId(task.id) }
            val comments = withContext(Dispatchers.IO){ dataSourceLocal.commentDao().getByTaskId(task.id) }
            val attachments = withContext(Dispatchers.IO){ dataSourceLocal.attachmentDao().getByTaskId(task.id) }
            val checklists = withContext(Dispatchers.IO){ dataSourceLocal.checklistDao().getByTaskId(task.id) }
            val labels = withContext(Dispatchers.IO){ dataSourceLocal.labelDao().getByTaskId(task.id) }

            dataTask.add(
                DataTask(
                    id = task.id,
                    title = task.title,
                    deadline = task.deadline,
                    description = task.description,
                    priority = task.priority,
                    status = task.status,
                    pic = pic,
                    project = project!!,
                    teams = teams,
                    comments = comments,
                    attachments = attachments,
                    checklists = checklists,
                    labels = labels
                )
            )
        }

        val listTaskDRO = ListTaskDRO(
            status = "200",
            message = message,
            data = dataTask
        )

        return listTaskDRO
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

            message = "Success get project by id from API!"
        }catch (e: Exception){}

        var getMemberFromLocal = withContext(Dispatchers.IO){dataSourceLocal.userDao().getByProjectId(projectId.toInt())}

        val listUserDRO = ListUserDRO(
            status = "200",
            message = message,
            data = getMemberFromLocal
        )

        return listUserDRO
    }

    suspend fun createTask(taskDTO: addTaskDTO): BasicDRO {
        var createTask: BasicDRO? = null

        try {
            createTask = dataSourceRemote.createTask(taskDTO)

            if(createTask.status == "201"){
                val task = Tasks(
                    id = createTask.data.toString().toInt(),
                    title = taskDTO.title,
                    deadline = taskDTO.deadline,
                    description = taskDTO.description,
                    priority = if(taskDTO.priority == "Low") 0 else if(taskDTO.priority == "Medium") 1 else 2,
                    status = 0,
                    pic_email = taskDTO.pic_email,
                    project_id = taskDTO.project_id.toInt()
                )

                withContext(Dispatchers.IO){
                    dataSourceLocal.taskDao().insert(task)
                }

                if(taskDTO.task_team != null){
                    for(team in taskDTO.task_team){
                        val createTeam = TaskTeams(
                            task_id = createTask.data.toString().toInt(),
                            team_email = team
                        )

                        withContext(Dispatchers.IO){
                            dataSourceLocal.taskTeamDao().insert(createTeam)
                        }
                    }
                }

            }else{
                createTask = null
            }

        }catch(e: Exception){}

        return createTask!!
    }
}