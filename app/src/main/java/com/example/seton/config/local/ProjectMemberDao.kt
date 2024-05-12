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

    @Query("SELECT u.email AS email, u.name AS name, u.profile_picture AS profile_picture, u.password AS password, u.auth_token AS auth_token, u.status AS status FROM project_members pm INNER JOIN users u ON u.email = pm.member_email WHERE project_id = :id")
    fun getUserByProjectId(id: Int): List<Users>

    @Query("DELETE FROM project_members")
    fun clearProjectMembers()

}