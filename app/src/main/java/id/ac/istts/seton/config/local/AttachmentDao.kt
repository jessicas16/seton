package id.ac.istts.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import id.ac.istts.seton.entity.Attachments

@Dao
interface AttachmentDao {
    @Insert
    fun insert(attachment: Attachments)

    @Update
    fun update(attachment: Attachments)

    @Delete
    fun delete(attachment: Attachments)

    @Query("SELECT * FROM attachments WHERE task_id = :id")
    fun getByTaskId(id: Int): List<Attachments>
}