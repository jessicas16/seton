package id.ac.istts.seton.config

import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ListProjectDRO
import id.ac.istts.seton.entity.ListTaskDRO
import id.ac.istts.seton.entity.ListUserDRO
import id.ac.istts.seton.entity.ProjectDRO
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.entity.addProjectDTO
import id.ac.istts.seton.entity.addTaskDTO
import id.ac.istts.seton.entity.userDTO
import id.ac.istts.seton.entity.userLoginDTO
import retrofit2.http.*

interface ApiService {
    //TEST
    @GET("checkConnection")
    suspend fun checkConnection():BasicDRO

    //USER
    @GET("users")
    suspend fun getAllUser(@Query("q") q:String = ""):List<Users>

    @GET("users/except/{email}")
    suspend fun getAllUserExceptLoginUser(@Path("email") email: String): ListUserDRO

    @GET("users/{email}")
    suspend fun checkEmail(@Path("email") email: String): UserDRO

    @POST("users/register")
    suspend fun registerUser(
        @Body userDTO : userDTO
    ): BasicDRO

    @POST("users/login/")
    suspend fun loginUser(@Body userLoginDRO: userLoginDTO):BasicDRO

    //PROJECTS
    @GET("projects/{email}")
    suspend fun getUserProjects(@Path("email") email: String): ListProjectDRO

    @POST("projects/")
    suspend fun createProject(@Body projectDTO: addProjectDTO): BasicDRO

    @GET("projects/getById/{projectId}")
    suspend fun getProjectById(@Path("projectId") projectId: String): ProjectDRO

    @GET("projects/getDetail/{projectId}")
    suspend fun getProjectDetail(@Path("projectId") projectId: String): ProjectDetailDRO

    @POST("projects/addMember/{projectId}/{email}")
    suspend fun addMemberProject(
        @Path("projectId") projectId: String,
        @Path("email") email: String
    ): BasicDRO

    @DELETE("projects/deleteMember/{projectId}/{email}")
    suspend fun deleteMemberProject(
        @Path("projectId") projectId: String,
        @Path("email") email: String
    ): BasicDRO

    //TASKS
    @GET("tasks/user/{email}")
    suspend fun getUserTasks(@Path("email") email: String): ListTaskDRO

    @GET("tasks/project/getMembers/{projectId}")
    suspend fun getProjectMembers(@Path("projectId") projectId: String): ListUserDRO

    @POST("tasks/")
    suspend fun createTask(@Body taskDTO: addTaskDTO): BasicDRO
}