package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.TaskTeams

@Dao
interface TaskTeamDao {
    @Insert
    fun insert(member: TaskTeams)

    @Update
    fun update(member: TaskTeams)

    @Delete
    fun delete(member: TaskTeams)

    @Query("SELECT * FROM task_teams WHERE task_id = :id AND team_email = :email")
    fun checkByTaskIdAndEmail(id: Int, email: String): TaskTeams?

}