package id.ac.istts.seton.entity

import com.squareup.moshi.JsonClass
import id.ac.istts.seton.mainPage.DataTaskDashboard
import id.ac.istts.seton.projectPage.DataProject
import id.ac.istts.seton.projectPage.DetailProject
import id.ac.istts.seton.taskPage.DataTask

@JsonClass(generateAdapter = true)
data class BasicDRO(
    var status : String,
    var message: String,
    var data : Any?
)

data class ListProjectDRO(
    var status : String,
    var message: String,
    var data : List<DataProject>
)

data class ListTaskDRO(
    var status : String,
    var message: String,
    var data : List<DataTask>
)

data class ListTaskDashboardDRO(
    var status: String,
    var message: String,
    var data : List<DataTaskDashboard>
)

data class ListUserDRO(
    var status : String,
    var message: String,
    var data : List<Users>
)

data class UserDRO(
    var status : String,
    var message: String,
    var data : Users
)

data class ProjectDRO(
    var status : String,
    var message: String,
    var data : Projects
)

data class ProjectDetailDRO(
    var status : String,
    var message: String,
    var data : DetailProject,
)