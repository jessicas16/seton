package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "checklists")
data class Checklists(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "is_checked") var is_checked:Int,
    @ColumnInfo(name = "task_id") var task_id:String
)