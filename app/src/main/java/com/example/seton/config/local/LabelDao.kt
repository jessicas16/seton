package com.example.seton.config.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.seton.entity.Labels

@Dao
interface LabelDao {
    @Insert
    fun insert(label: Labels)

    @Update
    fun update(label: Labels)

    @Delete
    fun delete(label: Labels)
}