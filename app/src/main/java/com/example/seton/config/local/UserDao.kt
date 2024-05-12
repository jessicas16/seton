package com.example.seton.config.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Users

interface UserDao {
    @Insert
    fun insert(user: Users)

    @Update
    fun update(user: Users)

    @Delete
    fun delete(user: Users)
}