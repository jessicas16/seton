package id.ac.istts.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class userDTO (
    val name: String,
    val email: String,
    val password: String
)

data class userLoginDTO(
    val email: String,
    val password: String
)

data class addProjectDTO(
    val name: String,
    val description: String,
    val startTime : String,
    val deadline: String,
    val members_email: List<String>?,
    val pm_email: String
)

data class addTaskDTO(
    val title: String,
    val description: String,
    val deadline: String,
    val task_team: List<String>?,
    val priority : String,
    val pic_email: String,
    val project_id : String
)