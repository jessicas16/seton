package id.ac.istts.seton.config

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import id.ac.istts.seton.config.local.AppDatabase
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ChecklistDRO
import id.ac.istts.seton.entity.Checklists
import id.ac.istts.seton.entity.LabelDRO
import id.ac.istts.seton.entity.Labels
import id.ac.istts.seton.entity.ListProjectDRO
import id.ac.istts.seton.entity.ListTaskDRO
import id.ac.istts.seton.entity.ListTaskDashboardDRO
import id.ac.istts.seton.entity.ListUserDRO
import id.ac.istts.seton.entity.ProjectDRO
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.ProjectMembers
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.Remember
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.TaskTeams
import id.ac.istts.seton.entity.Tasks
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.entity.addProjectDTO
import id.ac.istts.seton.entity.addTaskDTO
import id.ac.istts.seton.entity.userDTO
import id.ac.istts.seton.entity.userLoginDTO
import id.ac.istts.seton.loginRegister.authUser
import id.ac.istts.seton.mainPage.DataTaskDashboard
import id.ac.istts.seton.projectPage.DataProject
import id.ac.istts.seton.projectPage.DetailProject
import id.ac.istts.seton.taskPage.DataTask
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

    suspend fun getAllUserExceptLoginUser(force:Boolean = false, email: String): ListUserDRO {
        return dataSourceRemote.getAllUserExceptLoginUser(email)
    }

    suspend fun checkEmail(email: String): UserDRO {
        return dataSourceRemote.checkEmail(email)
    }

    suspend fun registerUser(userDTO : userDTO): BasicDRO {
        return dataSourceRemote.registerUser(userDTO)
    }

    suspend fun registerUserWithGoogle(user:authUser): BasicDRO {
        return dataSourceRemote.registerUserWithGoogle(user)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun loginUserWithGoogle(user:authUser):BasicDRO {
        var loginUser: BasicDRO? = null

        try {
            loginUser = dataSourceRemote.loginUserWithGoogle(user)
        }catch (e: Exception){

        }
        Log.i("LOGIN USER", loginUser.toString())
        withContext(Dispatchers.IO){
            if(loginUser != null){
                val expired = LocalDate.now().plusDays(30).toString()
                val remember = Remember(
                    email = user.email,
                    expired = expired
                )
                dataSourceLocal.rememberDao().clearDb()
                dataSourceLocal.rememberDao().insert(remember)
            }
        }

        return loginUser!!
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
    suspend fun loginUser(userLoginDTO: userLoginDTO):BasicDRO {
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
    suspend fun getUserProjects(force:Boolean = true, email: String ): ListProjectDRO {
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
                    getUserProjects(true, email)
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
                            withContext(Dispatchers.IO){ dataSourceLocal.userDao().insert(team)}
                        }catch (e: Exception){}

                        try {
                            withContext(Dispatchers.IO) {
                                if (dataSourceLocal.taskTeamDao()
                                        .checkByTaskIdAndEmail(data.id, team.email) == null
                                ) {
                                    val taskTeam = TaskTeams(
                                        task_id = data.id,
                                        team_email = team.email
                                    )
                                    dataSourceLocal.taskTeamDao().insert(taskTeam)
                                }
                            }
                        }catch (e: Exception){}
                    }

                    for(comment in data.comments){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.commentDao().insert(comment)}
                        }catch (e: Exception){}
                    }

                    for(attachment in data.attachments){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.attachmentDao().insert(attachment)}
                        }catch (e: Exception){}
                    }

                    for(checklist in data.checklists){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.checklistDao().insert(checklist)}
                        }catch (e: Exception){}
                    }

                    for(label in data.labels){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.labelDao().insert(label)}
                        }catch (e: Exception){}
                    }
                }
                message = "Success get project by id from API!"
            }catch (e: Exception){}
        }

        val getUserTasksByPICFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByPIC(email)}
        val getUserTasksByTeamFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByTeam(email)}

        val tasks: MutableList<Tasks> = mutableListOf()
        tasks.addAll(getUserTasksByPICFromLocal)
        tasks.addAll(getUserTasksByTeamFromLocal)

        val dataTask: MutableList<DataTask> = mutableListOf()
        getUserProjects(true, email)
        try {
            for(task in tasks){
                val pic = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByEmail(task.pic_email) }
                val projects = withContext(Dispatchers.IO){ dataSourceLocal.projectDao().getById(task.project_id)}
                val teams = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByTaskId(task.id) }
                val comments = withContext(Dispatchers.IO){ dataSourceLocal.commentDao().getByTaskId(task.id) }
                val attachments = withContext(Dispatchers.IO){ dataSourceLocal.attachmentDao().getByTaskId(task.id) }
                val checklists = withContext(Dispatchers.IO){ dataSourceLocal.checklistDao().getByTaskId(task.id) }
                val labels = withContext(Dispatchers.IO){ dataSourceLocal.labelDao().getByTaskId(task.id) }

                val data = DataTask(
                    id = task.id,
                    title = task.title,
                    deadline = task.deadline,
                    description = task.description,
                    priority = task.priority,
                    status = task.status,
                    pic = pic,
                    project = projects!!,
                    teams = teams,
                    comments = comments,
                    attachments = attachments,
                    checklists = checklists,
                    labels = labels
                )
                dataTask.add(data)
            }
        }catch (e: Exception){
            Log.i("ERRORMESSAGE", e.message.toString())
        }

        val listTaskDRO = ListTaskDRO(
            status = "200",
            message = message,
            data = dataTask
        )

        return listTaskDRO
    }

    suspend fun getUserTasksDashboard(force:Boolean = true, email: String = "ivan.s21@mhs.istts.ac.id"): ListTaskDashboardDRO {
        var message = "Success get project by id from local!"
        getUserProjects(true, email)
        if(force){
            try {
                val getUserTaskFromApi = withContext(Dispatchers.IO){
//                    Log.i("TOP_VALUEX", "---")
                    dataSourceRemote.getUserTasks(email)
                }

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

                    Log.i("DATA_TASK_REPO", data.teams.toString())
                    for(team in data.teams){
                        try {
                            withContext(Dispatchers.IO){ dataSourceLocal.userDao().insert(team)}
                            Log.i("TASK SUCCESS", "SUCCESS INSERT TEAM")
                        }catch (e: Exception){
                            Log.i("TASK ERROR", e.message.toString())
                        }

                        try {
                            withContext(Dispatchers.IO) {
                                if (dataSourceLocal.taskTeamDao()
                                        .checkByTaskIdAndEmail(data.id, team.email) == null
                                ) {
                                    val taskTeam = TaskTeams(
                                        task_id = data.id,
                                        team_email = team.email
                                    )
                                    dataSourceLocal.taskTeamDao().insert(taskTeam)
                                }
                            }
                            Log.i("TASK2 SUCCESS", "SUCCESS INSERT TEAM")
                        }catch (e: Exception){
                            Log.i("TASK2 ERROR", e.message.toString())
                        }
                    }

                    for(comment in data.comments){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.commentDao().insert(comment)}
                        }catch (e: Exception){}
                    }

                    for(attachment in data.attachments){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.attachmentDao().insert(attachment)}
                        }catch (e: Exception){}
                    }

                    for(checklist in data.checklists){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.checklistDao().insert(checklist)}
                        }catch (e: Exception){}
                    }

                    for(label in data.labels){
                        try {
                            withContext(Dispatchers.IO){dataSourceLocal.labelDao().insert(label)}
                        }catch (e: Exception){}
                    }

                    getUserProjects(true, email)
                }
                message = "Success get project by id from API!"
            }catch (e: Exception){
                Log.i("ERR_REPO", e.message.toString())
            }
        }

        val getUserTasksByPICFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByPIC(email)}
        val getUserTasksByTeamFromLocal = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getByTeam(email)}

        val tasks: MutableList<Tasks> = mutableListOf()
        tasks.addAll(getUserTasksByPICFromLocal)
        tasks.addAll(getUserTasksByTeamFromLocal)

        val dataTaskDashboard: MutableList<DataTaskDashboard> = mutableListOf()

        for(task in tasks){
            val pic = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByEmail(task.pic_email) }
            val project = withContext(Dispatchers.IO){ dataSourceLocal.projectDao().getById(task.project_id)}
            val teams = withContext(Dispatchers.IO){ dataSourceLocal.userDao().getByTaskId(task.id) }
            val comments = withContext(Dispatchers.IO){ dataSourceLocal.commentDao().getByTaskId(task.id) }
            val attachments = withContext(Dispatchers.IO){ dataSourceLocal.attachmentDao().getByTaskId(task.id) }
            val checklists = withContext(Dispatchers.IO){ dataSourceLocal.checklistDao().getByTaskId(task.id) }
            val labels = withContext(Dispatchers.IO){ dataSourceLocal.labelDao().getByTaskId(task.id) }

            dataTaskDashboard.add(
                DataTaskDashboard(
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

        val listTaskDashboardDRO = ListTaskDashboardDRO(
            status = "200",
            message = message,
            data = dataTaskDashboard
        )

        return listTaskDashboardDRO
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

    suspend fun getUserPIC(emailList: List<String>): List<Users>{
        var userList = arrayListOf<Users>()

        Log.d("aaaaa", emailList.toString())
        for(email in emailList){
            userList.add(withContext(Dispatchers.IO){dataSourceLocal.userDao().getByEmail(email)})
        }
        return userList
    }

    suspend fun getTaskDetail(taskId : String): TaskDRO {
        var TaskDRO: TaskDRO? = null
        var message = "Success get task by id from local!"

        try {
            val getTaskDetailFormApi = withContext(Dispatchers.IO){dataSourceRemote.getTaskById(taskId)}
            val data = getTaskDetailFormApi.data

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
                    } else {
                        dataSourceLocal.taskDao().update(task)
                    }
                }catch (e: Exception){
                    Log.i("ERRORMESSAGE", e.message.toString())
                }
            }

            for(team in data.teams){
                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.userDao().update(team)
                    }catch (e: Exception){}
                }

                withContext(Dispatchers.IO){
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
            }

            for(comment in data.comments){
                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.commentDao().insert(comment)
                    }catch (e: Exception){}
                }
            }

            for(attachment in data.attachments){
                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.attachmentDao().insert(attachment)
                    }catch (e: Exception){}
                }
            }

            for(checklist in data.checklists){
                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.checklistDao().insert(checklist)
                    }catch (e: Exception){}
                }
            }

            for(label in data.labels){
                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.labelDao().insert(label)
                    }catch (e: Exception){}
                }
            }

            message = "Success get task by id from API!"
        }catch (e: Exception){
            Log.i("ERRORMESSAGE", e.message.toString())
        }

        val task = withContext(Dispatchers.IO){dataSourceLocal.taskDao().getById(taskId.toInt())}
        Log.i("2taskasdfadffasddfssdfdfsfsddf", task.toString())
        val pic = withContext(Dispatchers.IO){dataSourceLocal.userDao().getByEmail(task!!.pic_email)}
        val project = withContext(Dispatchers.IO){dataSourceLocal.projectDao().getById(task!!.project_id)}
        val teams = withContext(Dispatchers.IO){dataSourceLocal.userDao().getByTaskId(taskId.toInt())}
        val comments = withContext(Dispatchers.IO){dataSourceLocal.commentDao().getByTaskId(taskId.toInt())}
        val attachments = withContext(Dispatchers.IO){dataSourceLocal.attachmentDao().getByTaskId(taskId.toInt())}
        val checklists = withContext(Dispatchers.IO){dataSourceLocal.checklistDao().getByTaskId(taskId.toInt())}
        val labels = withContext(Dispatchers.IO){dataSourceLocal.labelDao().getByTaskId(taskId.toInt())}

        val dataTask = DataTask(
            id = task!!.id,
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

        TaskDRO = TaskDRO(
            status = "200",
            message = message,
            data = dataTask
        )

        return TaskDRO
    }

    suspend fun updateTaskStatus(taskId: String, status: String): BasicDRO {
        //update in db
        val x = withContext(Dispatchers.IO){
            dataSourceRemote.updateTaskStatus(taskId, status)
        }
        if(x.status != "200"){
            return x
        } else {
            //update in local
            try {
                val getTaskDetailFormApi = dataSourceRemote.getTaskById(taskId)
                val data = getTaskDetailFormApi.data

                val task = Tasks(
                    id = data.id,
                    title = data.title,
                    deadline = data.deadline,
                    description = data.description,
                    priority = data.priority,
                    status = status.toInt(),
                    pic_email = data.pic.email,
                    project_id = data.project.id
                )

                withContext(Dispatchers.IO){
                    try {
                        dataSourceLocal.taskDao().update(task)
                    }catch (e: Exception){}
                }
            } catch (e: Exception) {
                Log.i("ERRORMESSAGE", e.message.toString())
            }

            var dro = BasicDRO(
                status = "200",
                message = "Success update task status!",
                data = ""
            )
            return dro
        }
    }

    suspend fun addLabelToTask(taskId: String, label: String): LabelDRO {
        var addLabel: LabelDRO? = null

        try {
            addLabel = dataSourceRemote.addLabelToTask(taskId, label)

            if(addLabel.status == "201"){
                val labelBaru = Labels(
                    id = addLabel.data.id,
                    title = addLabel.data.title,
                    color = addLabel.data.color,
                    task_id = addLabel.data.task_id
                )

                withContext(Dispatchers.IO){
                    dataSourceLocal.labelDao().insert(labelBaru)
                }
            }else{
                addLabel = null
            }
        }catch (e: Exception){}
        return addLabel!!
    }

    suspend fun addChecklist(taskId: String, title: String): ChecklistDRO {
        var addCheck: ChecklistDRO? = null

        try {
            addCheck = dataSourceRemote.addChecklist(taskId, title)

            if(addCheck.status == "201"){
                val checklist = Checklists(
                    id = addCheck.data.id,
                    title = addCheck.data.title,
                    is_checked = addCheck.data.is_checked,
                    task_id = addCheck.data.task_id
                )

                withContext(Dispatchers.IO){
                    dataSourceLocal.checklistDao().insert(checklist)
                }
            }else{
                addCheck = null
            }
        }catch (e: Exception){}
        return addCheck!!
    }
}