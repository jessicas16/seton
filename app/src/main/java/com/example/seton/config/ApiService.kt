package com.example.seton.config

import com.example.seton.entity.*
import org.json.JSONObject
import retrofit2.http.*

interface ApiService {
    //TEST
    @GET("checkConnection")
    suspend fun checkConnection():BasicDRO

    //USER
    @GET("users")
    suspend fun getAllUser(@Query("q") q:String = ""):List<Users>

    @POST("users/register")
    suspend fun registerUser(
        @Body userDTO : userDTO
    ): BasicDRO

    @POST("users/login/")
    suspend fun loginUser(@Body userLoginDRO:userLoginDTO):BasicDRO

    //PROJECTS
    @GET("projects/{email}")
    suspend fun getUserProjects(@Path("email") email: String): BasicDRO
}