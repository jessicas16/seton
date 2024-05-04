package com.example.seton.config

import com.example.seton.entity.*
import org.json.JSONObject

class DefaultRepo(
    private val dataSourceRemote : ApiService,
) {
    //TEST CONNECTION
    suspend fun checkConnection():BasicDTO {
        return dataSourceRemote.checkConnection()
    }

    //USER
    suspend fun getAllUser(force:Boolean = false):List<Users> {
        return dataSourceRemote.getAllUser()
    }

    suspend fun registerUser(userDRO : userDRO): BasicDTO {
        return dataSourceRemote.registerUser(userDRO)
    }

    suspend fun loginUser(userLoginDRO:userLoginDRO):BasicDTO {
        return dataSourceRemote.loginUser(userLoginDRO)
    }

    //PROJECTS
    suspend fun getUserProjects(force:Boolean = false, email: String = "ivan.s21@mhs.istts.ac.id"): BasicDTO {
        return dataSourceRemote.getUserProjects(email)
    }
}