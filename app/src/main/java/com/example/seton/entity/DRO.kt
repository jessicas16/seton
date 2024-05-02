package com.example.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class userDRO (
    val name: String,
    val email: String,
    val password: String,
)

data class userLoginDRO(
    val email: String,
    val password: String,
)