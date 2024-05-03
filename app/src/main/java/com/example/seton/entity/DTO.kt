package com.example.seton.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BasicDTO(
    var status : String,
    var message: String,
    var data : Any?
)