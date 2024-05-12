package com.example.seton.entity

import com.example.seton.projectPage.DataProject
import com.example.seton.projectPage.DetailProject
import com.example.seton.taskPage.DataTask
import com.squareup.moshi.JsonClass

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