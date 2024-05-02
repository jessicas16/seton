package com.example.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") val email:String,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "profile_picture") var profile_picture:String,
    @ColumnInfo(name = "password") var password:String,
    @ColumnInfo(name = "auth_token") var auth_token:String,
    @ColumnInfo(name = "status") var status:Int
)
