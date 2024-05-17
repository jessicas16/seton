package com.example.seton.config.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.seton.entity.Attachments
import com.example.seton.entity.Checklists
import com.example.seton.entity.Comments
import com.example.seton.entity.Labels
import com.example.seton.entity.ProjectMembers
import com.example.seton.entity.Projects
import com.example.seton.entity.Remember
import com.example.seton.entity.Tasks
import com.example.seton.entity.Users

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
        Tasks::class
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