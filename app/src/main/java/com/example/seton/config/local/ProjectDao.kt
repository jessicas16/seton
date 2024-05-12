package com.example.seton.config.local

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Projects

interface ProjectDao {
    @Insert
    fun insert(project: Projects)

    @Update
    fun update(project: Projects)

    @Delete
    fun delete(project: Projects)
}