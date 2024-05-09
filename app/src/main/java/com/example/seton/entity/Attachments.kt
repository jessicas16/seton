package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "attachments")
data class Attachments(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "file_name") var file_name:String,
    @ColumnInfo(name = "upload_time") var upload_time:String,
    @ColumnInfo(name = "task_id") var task_id:String
)