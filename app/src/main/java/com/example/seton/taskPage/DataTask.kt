package com.example.seton.taskPage

import com.example.seton.entity.Attachments
import com.example.seton.entity.Checklists
import com.example.seton.entity.Comments
import com.example.seton.entity.Labels
import com.example.seton.entity.Projects
import com.example.seton.entity.Users
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DataTask (
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