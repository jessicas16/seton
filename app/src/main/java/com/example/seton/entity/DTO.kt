package com.example.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasicDTO(
    val status : String,
    val message: String,
    val data : Any?
)