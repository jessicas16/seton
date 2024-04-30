package com.example.seton.entity

import androidx.room.*
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "User")
data class userEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") val email:String,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "profile_picture") var profile_picture:String,
    @ColumnInfo(name = "password") var password:String,
    @ColumnInfo(name = "auth_token") var auth_token:String,
    @ColumnInfo(name = "status") var status:Int,
)