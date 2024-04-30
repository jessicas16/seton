package com.example.seton.config

import com.example.seton.entity.userEntity
import retrofit2.http.*

interface ApiService {
    //USER
    @GET("users")
    suspend fun getAllUser(@Query("q") q:String = ""):String

    @POST("users/register/")
    suspend fun registerUser(
        @Body email:String, name:String, password:String
    ):String

    @POST("users/login/")
    suspend fun loginUser(@Body user:userEntity):userEntity
}