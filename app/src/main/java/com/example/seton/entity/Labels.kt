package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "labels")
data class Labels(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "color") var color:String,
    @ColumnInfo(name = "task_id") var task_id:String
)