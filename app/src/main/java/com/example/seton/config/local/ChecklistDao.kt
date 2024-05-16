package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Checklists

@Dao
interface ChecklistDao {
    @Insert
    fun insert(checklist: Checklists)

    @Update
    fun update(checklist: Checklists)

    @Delete
    fun delete(checklist: Checklists)
}