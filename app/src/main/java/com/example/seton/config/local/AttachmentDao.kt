package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
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
}