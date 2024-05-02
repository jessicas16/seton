package com.example.seton.config

import com.example.seton.entity.BasicDTO
import com.example.seton.entity.Users
import com.example.seton.entity.addUserDTO
import com.example.seton.entity.userDRO
import retrofit2.http.*

interface ApiService {
    //TEST
    @GET("test")
    suspend fun test2():BasicDTO

    //USER
    @GET("users")
    suspend fun getAllUser(@Query("q") q:String = ""):List<Users>

    @POST("users/register")
    suspend fun registerUser(
        @Body userDRO : userDRO
    ): addUserDTO

    @POST("users/login/")
    suspend fun loginUser(@Body user:Users):Users
}