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

    @Query("SELECT u.email AS email, u.name AS name, u.profile_picture AS profile_picture, u.password AS password, u.auth_token AS auth_token, u.status AS status FROM users u INNER JOIN project_members pm ON u.email = pm.member_email WHERE pm.project_id = :id")
    fun getByProjectId(id: Int): List<Users>

    @Query("SELECT u.email AS email, u.name AS name, u.profile_picture AS profile_picture, u.password AS password, u.auth_token AS auth_token, u.status AS status FROM users u INNER JOIN task_teams tm ON u.email = tm.team_email WHERE tm.task_id = :id")
    fun getByTaskId(id: Int): List<Users>

    @Query("DELETE FROM users")
    fun clearUsers()
}