package com.example.seton.config

import com.example.seton.entity.*

class DefaultRepo(
    private val dataSourceRemote : ApiService,
) {
    suspend fun test2():BasicDTO {
        return dataSourceRemote.test2()
    }
    suspend fun getAllUser(force:Boolean = false):List<Users> {
        return dataSourceRemote.getAllUser()
    }

    suspend fun registerUser(userDRO : userDRO): BasicDTO {
        return dataSourceRemote.registerUser(userDRO)
    }

}