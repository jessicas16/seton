package com.example.seton.config

import com.example.seton.entity.*
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
    ): BasicDTO

    @POST("users/login/")
    suspend fun loginUser(@Body user:Users):Users
}