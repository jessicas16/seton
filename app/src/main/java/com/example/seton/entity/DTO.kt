package com.example.seton.entity

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