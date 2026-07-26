package com.aiher.app.data.local

import androidx.room.*

@Database(
    entities = [
        com.aiher.app.data.local.ProjectEntity::class,
        com.aiher.app.data.local.MessageEntity::class,
        com.aiher.app.data.local.AppEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun messageDao(): MessageDao
    abstract fun appDao(): AppDao
}

// ============ Entities ============

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val packageName: String,
    val createdAt: Long,
    val updatedAt: Long,
    val status: String
)

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val role: String,
    val content: String,
    val timestamp: Long,
    val generatedApkPath: String?,
    val appName: String?
)

@Entity(tableName = "generated_apps")
data class AppEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val packageName: String,
    val apkPath: String?,
    val iconUrl: String?,
    val status: String,
    val createdAt: Long,
    val updatedAt: Long
)

// ============ DAOs ============

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY updatedAt DESC")
    suspend fun getAllProjects(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Delete
    suspend fun deleteProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :projectId")
    suspend fun deleteProjectById(projectId: String)
}

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE projectId = :projectId ORDER BY timestamp ASC")
    suspend fun getMessagesByProject(projectId: String): List<MessageEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<MessageEntity>)

    @Query("DELETE FROM messages WHERE projectId = :projectId")
    suspend fun deleteMessagesByProject(projectId: String)
}

@Dao
interface AppDao {
    @Query("SELECT * FROM generated_apps ORDER BY updatedAt DESC")
    suspend fun getAllApps(): List<AppEntity>

    @Query("SELECT * FROM generated_apps WHERE id = :appId")
    suspend fun getAppById(appId: String): AppEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApp(app: AppEntity)

    @Delete
    suspend fun deleteApp(app: AppEntity)

    @Query("UPDATE generated_apps SET status = :status, apkPath = :apkPath WHERE id = :appId")
    suspend fun updateAppStatus(appId: String, status: String, apkPath: String?)
}