package com.aiher.app.data.repository

import com.aiher.app.data.local.AppDao
import com.aiher.app.data.local.ProjectDao
import com.aiher.app.data.local.ProjectEntity
import com.aiher.app.data.model.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao,
    private val appDao: AppDao
) {

    suspend fun getAllProjects(): List<Project> {
        return projectDao.getAllProjects().map { it.toProject() }
    }

    suspend fun getProjectById(id: String): Project? {
        return projectDao.getProjectById(id)?.toProject()
    }

    suspend fun createProject(name: String, description: String = ""): Project {
        val project = Project(
            name = name,
            description = description,
            packageName = generatePackageName(name)
        )
        projectDao.insertProject(project.toEntity())
        return project
    }

    suspend fun updateProject(project: Project) {
        projectDao.insertProject(project.toEntity())
    }

    suspend fun deleteProject(projectId: String) {
        projectDao.deleteProjectById(projectId)
    }

    suspend fun getAllGeneratedApps(): List<GeneratedApp> {
        return appDao.getAllApps().map { it.toGeneratedApp() }
    }

    suspend fun getAppById(appId: String): GeneratedApp? {
        return appDao.getAppById(appId)?.toGeneratedApp()
    }

    suspend fun deleteApp(appId: String) {
        appDao.getAppById(appId)?.let { appDao.deleteApp(it) }
    }

    private fun generatePackageName(name: String): String {
        val sanitized = name.lowercase()
            .replace(Regex("[^a-z0-9]"), "")
            .take(20)
        return "com.aiher.project.$sanitized"
    }

    private fun ProjectEntity.toProject(): Project {
        return Project(
            id = id,
            name = name,
            description = description,
            packageName = packageName,
            createdAt = createdAt,
            updatedAt = updatedAt,
            status = ProjectStatus.valueOf(status)
        )
    }

    private fun Project.toEntity(): ProjectEntity {
        return ProjectEntity(
            id = id,
            name = name,
            description = description,
            packageName = packageName,
            createdAt = createdAt,
            updatedAt = updatedAt,
            status = status.name
        )
    }

    private fun com.aiher.app.data.local.AppEntity.toGeneratedApp(): GeneratedApp {
        return GeneratedApp(
            id = id,
            name = name,
            description = description,
            packageName = packageName,
            apkPath = apkPath,
            iconUrl = iconUrl,
            status = AppStatus.valueOf(status),
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}