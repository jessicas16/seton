package com.example.seton.DAO

import androidx.room.*
import com.example.seton.entity.userEntity

@Dao
interface userDao {
    @Query("SELECT * FROM User")
    fun fetchAll():userEntity

    @Insert
    fun insert(u:userEntity)

    @Update
    fun update(u:userEntity)

    @Delete
    fun delete(u:userEntity)
}