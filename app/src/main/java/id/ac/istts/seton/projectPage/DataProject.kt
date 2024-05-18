package id.ac.istts.seton.projectPage

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import id.ac.istts.seton.entity.Tasks
import id.ac.istts.seton.entity.Users

@JsonClass(generateAdapter = true)
data class DataProject (
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "description") val description: String,
    @Json(name = "start") val start: String,
    @Json(name = "deadline") val deadline: String,
    @Json(name = "status") val status: Int,
    @Json(name = "owner") val owner: Users,
    @Json(name = "members") val members: List<Users>,
    @Json(name = "tasks") val tasks: List<Tasks>
)