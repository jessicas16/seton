package com.example.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasicDTO(
    val message: String
)

@JsonClass(generateAdapter = true)
data class addUserDTO(
    val message: String,
)