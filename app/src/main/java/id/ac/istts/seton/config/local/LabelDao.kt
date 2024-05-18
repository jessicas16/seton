package id.ac.istts.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import id.ac.istts.seton.entity.Labels

@Dao
interface LabelDao {
    @Insert
    fun insert(label: Labels)

    @Update
    fun update(label: Labels)

    @Delete
    fun delete(label: Labels)

    @Query("SELECT * FROM labels WHERE task_id = :id")
    fun getByTaskId(id: Int): List<Labels>
}