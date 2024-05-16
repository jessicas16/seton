package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.seton.entity.Attachments

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