package id.ac.istts.seton.mainPage

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import id.ac.istts.seton.entity.Attachments
import id.ac.istts.seton.entity.Checklists
import id.ac.istts.seton.entity.Comments
import id.ac.istts.seton.entity.Labels
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.Users

@JsonClass(generateAdapter = true)
data class DataTask(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "deadline") val deadline: String,
    @Json(name = "description") val description: String,
    @Json(name = "priority") val priority: Int,
    @Json(name = "status") val status: Int,
    @Json(name = "pic") val pic: Users,
    @Json(name = "project") val project: Projects,
    @Json(name = "teams") val teams: List<Users>,
    @Json(name = "comments") val comments: List<Comments>,
    @Json(name = "attachments") val attachments: List<Attachments>,
    @Json(name = "checklists") val checklists: List<Checklists>,
    @Json(name = "labels") val labels: List<Labels>
)