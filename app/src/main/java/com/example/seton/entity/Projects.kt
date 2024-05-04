package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
@Entity(tableName = "projects")
data class Projects(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "start") var start: Date,
    @ColumnInfo(name = "deadline") var deadline: Date,
    @ColumnInfo(name = "pm_email") var pmEmail: String,
    @ColumnInfo(name = "status") var status: Int
)
