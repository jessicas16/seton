package id.ac.istts.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remember")
data class Remember(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "email") var email: String,
    @ColumnInfo(name = "expired") var expired: String,
)
