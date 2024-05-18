package id.ac.istts.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import id.ac.istts.seton.entity.Tasks

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

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getById(id: Int): Tasks?

    @Query("SELECT * FROM tasks WHERE pic_email = :email")
    fun getByPIC(email: String): List<Tasks>

    @Query("SELECT t.id AS id, t.title AS title, t.deadline AS deadline, t.description AS description, t.priority AS priority, t.status AS status, t.pic_email AS pic_email, t.project_id AS project_id FROM tasks t INNER JOIN task_teams tt ON t.id = tt.task_id WHERE tt.team_email = :email ")
    fun getByTeam(email: String): List<Tasks>
}