package id.ac.istts.seton.projectPage

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import id.ac.istts.seton.entity.Tasks
import id.ac.istts.seton.entity.Users

@JsonClass(generateAdapter = true)
data class DetailProject (
    @Json(name = "projectName") val projectName: String,
    @Json(name = "projectDescription") val projectDescription: String,
    @Json(name = "projectStart") val projectStart: String,
    @Json(name = "projectDeadline") val projectDeadline: String,
    @Json(name = "projectManager") val projectManager: Users,
    @Json(name = "projectMembers") val members: List<Users>,
    @Json(name = "projectTasks") val tasks: List<Tasks>,
    @Json(name = "upcomingTask") val upcomingTask: Int,
    @Json(name = "ongoingTask") val ongoingTask: Int,
    @Json(name = "submittedTask") val submittedTask: Int,
    @Json(name = "revisionTask") val revisionTask: Int,
    @Json(name = "completedTask") val completedTask: Int,
    @Json(name = "projectStatus") val projectStatus: String = ""
)