package com.example.seton.config.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Tasks

interface TaskDao {
    @Insert
    fun insert(task: Tasks)

    @Update
    fun update(task: Tasks)

    @Delete
    fun delete(task: Tasks)
}