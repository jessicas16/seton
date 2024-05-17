package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.Projects

@Dao
interface ProjectDao {
    @Insert
    fun insert(project: Projects)

    @Update
    fun update(project: Projects)

    @Delete
    fun delete(project: Projects)

    @Query("SELECT * FROM projects")
    fun getAll(): List<Projects>

    @Query("SELECT * FROM projects WHERE pm_email = :email")
    fun getByOwner(email: String): List<Projects>

    @Query("SELECT p.id AS id, p.name AS name, p.description AS description, p.start AS start, p.deadline AS deadline, p.pm_email AS pm_email, p.status AS status FROM projects p INNER JOIN project_members pm ON p.id = pm.project_id WHERE pm.member_email = :email ")
    fun getByMember(email: String): List<Projects>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getById(id: Int): Projects?

    //@Query("SELECT p.id AS id, p.name AS name, p.description AS description, p.start AS start, p.deadline AS deadline, p.pm_email AS pm_email, p.status AS status FROM projects p INNER JOIN tasks t ON p.id = t.project_id")

    @Query("DELETE FROM projects")
    fun clearProjects()
}