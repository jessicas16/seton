package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.Tasks

@Dao
interface TaskDao {
    @Insert
    fun insert(task: Tasks)

    @Update
    fun update(task: Tasks)

    @Delete
    fun delete(task: Tasks)

    @Query("SELECT * FROM tasks WHERE project_id = :id")
    fun getByProjectId(id: Int): List<Tasks>

    @Query("DELETE FROM tasks")
    fun clearTasks()


}