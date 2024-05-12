package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.Users

@Dao
interface UserDao {
    @Insert
    fun insert(user: Users)

    @Update
    fun update(user: Users)

    @Delete
    fun delete(user: Users)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getByEmail(email: String): Users

    @Query("DELETE FROM users")
    fun clearUsers()
}