package com.example.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class userDTO (
    val name: String,
    val email: String,
    val password: String,
)

data class userLoginDTO(
    val email: String,
    val password: String,
)