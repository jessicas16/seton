package com.example.seton.config

import com.example.seton.entity.*

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

}