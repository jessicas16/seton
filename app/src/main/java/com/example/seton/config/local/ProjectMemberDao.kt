package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.ProjectMembers
import com.example.seton.entity.Tasks
import com.example.seton.entity.Users

@Dao
interface ProjectMemberDao {
    @Insert
    fun insert(member: ProjectMembers)

    @Update
    fun update(member: ProjectMembers)

    @Delete
    fun delete(member: ProjectMembers)

    @Query("SELECT * FROM project_members WHERE project_id = :id AND member_email = :email")
    fun checkByProjectIdAndEmail(id: Int, email: String): ProjectMembers?

    @Query("DELETE FROM project_members")
    fun clearProjectMembers()

}