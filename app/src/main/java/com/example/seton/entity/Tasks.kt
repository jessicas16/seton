package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
@Entity(tableName = "tasks")
data class Tasks(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "title") var title:String,
    @ColumnInfo(name = "deadline") var deadline:Date,
    @ColumnInfo(name = "description") var description:String,
    @ColumnInfo(name = "priority") var priority:Int,
    @ColumnInfo(name = "status") var status:Int,
    @ColumnInfo(name = "pic_email") var picEmail:String,
    @ColumnInfo(name = "project_id") var project_id:Int
)
