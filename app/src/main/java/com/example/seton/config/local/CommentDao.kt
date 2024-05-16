package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Comments

@Dao
interface CommentDao {
    @Insert
    fun insert(comment: Comments)

    @Update
    fun update(comment: Comments)

    @Delete
    fun delete(comment: Comments)
}