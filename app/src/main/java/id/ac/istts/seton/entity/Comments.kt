package id.ac.istts.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "comments")
data class Comments(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id:Int,
    @ColumnInfo(name = "value") var value:String,
    @ColumnInfo(name = "time") var time:String,
    @ColumnInfo(name = "user_email") var user_email:String,
    @ColumnInfo(name = "task_id") var task_id:String
)