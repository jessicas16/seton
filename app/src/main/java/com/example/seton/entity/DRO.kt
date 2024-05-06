package com.example.seton.entity

import com.example.seton.projectPage.DataProject
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