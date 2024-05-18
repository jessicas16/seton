package id.ac.istts.seton.config.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.istts.seton.entity.Attachments
import id.ac.istts.seton.entity.Checklists
import id.ac.istts.seton.entity.Comments
import id.ac.istts.seton.entity.Labels
import id.ac.istts.seton.entity.ProjectMembers
import id.ac.istts.seton.entity.Projects
import id.ac.istts.seton.entity.Remember
import id.ac.istts.seton.entity.TaskTeams
import id.ac.istts.seton.entity.Tasks
import id.ac.istts.seton.entity.Users

@Database(
    entities = [
        Attachments::class,
        Checklists::class,
        Comments::class,
        Labels::class,
        Projects::class,
        ProjectMembers::class,
        Tasks::class,
        Users::class,
        Remember::class,
        TaskTeams::class
    ],
    version = 3
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun attachmentDao(): AttachmentDao
    abstract fun checklistDao(): ChecklistDao
    abstract fun commentDao(): CommentDao
    abstract fun labelDao(): LabelDao
    abstract fun projectDao(): ProjectDao
    abstract fun projectMemberDao(): ProjectMemberDao
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    abstract fun rememberDao(): RememberDao
    abstract fun taskTeamDao(): TaskTeamDao
}