package id.ac.istts.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import id.ac.istts.seton.entity.Checklists

@Dao
interface ChecklistDao {
    @Insert
    fun insert(checklist: Checklists)

    @Update
    fun update(checklist: Checklists)

    @Delete
    fun delete(checklist: Checklists)

    @Query("SELECT * FROM checklists WHERE task_id = :id")
    fun getByTaskId(id: Int): List<Checklists>
}