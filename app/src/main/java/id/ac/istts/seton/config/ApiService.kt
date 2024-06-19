package id.ac.istts.seton.config

import id.ac.istts.seton.entity.AddCommentDTO
import id.ac.istts.seton.entity.AllChecklistDRO
import id.ac.istts.seton.entity.AllCommentDRO
import id.ac.istts.seton.entity.BasicDRO
import id.ac.istts.seton.entity.ChangePasswordDRO
import id.ac.istts.seton.entity.ChangePasswordDTO
import id.ac.istts.seton.entity.ChecklistDRO
import id.ac.istts.seton.entity.CommentDRO
import id.ac.istts.seton.entity.GetAttachmentDRO
import id.ac.istts.seton.entity.LabelDRO
import id.ac.istts.seton.entity.ListProjectDRO
import id.ac.istts.seton.entity.ListTaskDRO
import id.ac.istts.seton.entity.ListUserDRO
import id.ac.istts.seton.entity.PostAttachmentDRO
import id.ac.istts.seton.entity.ProjectDRO
import id.ac.istts.seton.entity.ProjectDetailDRO
import id.ac.istts.seton.entity.TaskDRO
import id.ac.istts.seton.entity.UserDRO
import id.ac.istts.seton.entity.Users
import id.ac.istts.seton.entity.addProjectDTO
import id.ac.istts.seton.entity.addTaskDTO
import id.ac.istts.seton.entity.userDTO
import id.ac.istts.seton.entity.userLoginDTO
import id.ac.istts.seton.loginRegister.authUser
import okhttp3.MultipartBody
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

    @POST("users/registerWithGoogle")
    suspend fun registerUserWithGoogle(
        @Body user : authUser
    ): BasicDRO

    @POST("users/login/")
    suspend fun loginUser(@Body userLoginDRO: userLoginDTO):BasicDRO

    @POST("users/loginWithGoogle")
    suspend fun loginUserWithGoogle(
        @Body user : authUser
    ):BasicDRO

    @PUT("users/password/{email}")
    suspend fun updatePassword(
        @Path("email") email: String,
        @Body dataPassword: ChangePasswordDTO
    ): ChangePasswordDRO

    //PROJECTS
    @GET("projects/getTasksProject/{projectId}")
    suspend fun getTasksProject(@Path("projectId") projectId: String): ListTaskDRO

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

    @GET("tasks/{taskId}")
    suspend fun getTaskById(@Path("taskId") taskId: String): TaskDRO

    @PUT("tasks/status/{taskId}/{status}")
    suspend fun updateTaskStatus(
        @Path("taskId") taskId: String,
        @Path("status") status: String,
    ): BasicDRO

    @POST("tasks/label/{taskId}/{title}")
    suspend fun addLabelToTask(
        @Path("taskId") taskId: String,
        @Path("title") title: String,
    ): LabelDRO

    @POST("tasks/checklist/{taskId}/{title}")
    suspend fun addChecklist(
        @Path("taskId") taskId: String,
        @Path("title") title: String,
    ): ChecklistDRO

    @Multipart
    @POST("tasks/attachment/{taskId}")
    suspend fun uploadFile(
        @Path("taskId") taskId: String,
        @Part filename: MultipartBody.Part
    ): PostAttachmentDRO

    @GET("tasks/attachment/{taskId}")
    suspend fun getAttachments(@Path("taskId") taskId: String): GetAttachmentDRO

    @GET("tasks/checklist/{taskId}")
    suspend fun getChecklist(@Path("taskId") taskId: String): AllChecklistDRO

    @PUT("tasks/checklist/{checklistId}")
    suspend fun updateChecklistStatus(
        @Path("checklistId") checklistId: String
    ): BasicDRO

    @DELETE("tasks/checklist/{checklistId}")
    suspend fun deleteChecklist(
        @Path("checklistId") checklistId: String
    ): BasicDRO

    @POST("tasks/comment")
    suspend fun addComment(@Body comment: AddCommentDTO): CommentDRO

    @GET("tasks/comment/{taskId}")
    suspend fun getComments(@Path("taskId") taskId: String): AllCommentDRO
}