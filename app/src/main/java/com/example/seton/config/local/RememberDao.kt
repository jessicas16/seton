package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.Remember

@Dao
interface RememberDao {
    @Insert
    fun insert(remember: Remember)

    @Update
    fun update(remember: Remember)

    @Delete
    fun delete(remember: Remember)

    @Query("SELECT * FROM remember")
    fun get(): Remember?

    @Query("DELETE FROM remember")
    fun clearDb()
}