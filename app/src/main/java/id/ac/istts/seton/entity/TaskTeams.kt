package id.ac.istts.seton.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "task_teams")
data class TaskTeams(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "task_id") val task_id: Int,
    @ColumnInfo(name = "team_email") val team_email: String
)
