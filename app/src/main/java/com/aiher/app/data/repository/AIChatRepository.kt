package com.aiher.app.data.repository

import com.aiher.app.data.local.*
import com.aiher.app.data.model.*
import com.aiher.app.data.remote.AIChatApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIChatRepository @Inject constructor(
    private val api: AIChatApi,
    private val messageDao: MessageDao,
    private val appDao: AppDao,
    private val settingsDataStore: SettingsDataStore
) {

    suspend fun sendMessage(
        projectId: String,
        userMessage: String,
        systemPrompt: String = getDefaultSystemPrompt()
    ): Result<ChatMessage> {
        return try {
            // 保存用户消息
            val userMsg = MessageEntity(
                id = java.util.UUID.randomUUID().toString(),
                projectId = projectId,
                role = MessageRole.USER.name,
                content = userMessage,
                timestamp = System.currentTimeMillis(),
                generatedApkPath = null,
                appName = null
            )
            messageDao.insertMessage(userMsg)

            // 获取历史消息
            val history = messageDao.getMessagesByProject(projectId)
            val apiMessages = mutableListOf(
                ChatCompletionMessage(role = "system", content = systemPrompt)
            )
            history.forEach { msg ->
                apiMessages.add(ChatCompletionMessage(role = msg.role.lowercase(), content = msg.content))
            }

            // 获取配置
            val modelName = ""
            val baseUrl = ""
            val apiKey = ""

            // 调用API
            val request = ChatCompletionRequest(
                model = "gpt-4",
                messages = apiMessages,
                temperature = 0.7f,
                max_tokens = 4096
            )

            val response = api.chatCompletion(request)

            if (response.isSuccessful) {
                val content = response.body()?.choices?.firstOrNull()?.message?.content ?: ""
                val assistantMsg = MessageEntity(
                    id = java.util.UUID.randomUUID().toString(),
                    projectId = projectId,
                    role = MessageRole.ASSISTANT.name,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    generatedApkPath = null,
                    appName = null
                )
                messageDao.insertMessage(assistantMsg)

                Result.success(
                    ChatMessage(
                        id = assistantMsg.id,
                        role = MessageRole.ASSISTANT,
                        content = content,
                        timestamp = assistantMsg.timestamp
                    )
                )
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateApp(
        projectId: String,
        appName: String,
        requirements: String
    ): Result<GeneratedApp> {
        val app = GeneratedApp(
            name = appName,
            description = requirements,
            packageName = generatePackageName(appName),
            status = AppStatus.GENERATING
        )

        // 保存到数据库
        appDao.insertApp(
            AppEntity(
                id = app.id,
                name = app.name,
                description = app.description,
                packageName = app.packageName,
                apkPath = null,
                iconUrl = null,
                status = AppStatus.GENERATING.name,
                createdAt = app.createdAt,
                updatedAt = app.updatedAt
            )
        )

        // 发送生成请求
        val prompt = buildAppGenerationPrompt(appName, requirements)
        return sendMessage(projectId, prompt, getAppGeneratorSystemPrompt()).map { _ ->
            // 模拟生成完成
            appDao.updateAppStatus(app.id, AppStatus.COMPLETED.name, null)
            app.copy(status = AppStatus.COMPLETED)
        }
    }

    suspend fun getProjectMessages(projectId: String): List<ChatMessage> {
        return messageDao.getMessagesByProject(projectId).map { entity ->
            ChatMessage(
                id = entity.id,
                role = MessageRole.valueOf(entity.role),
                content = entity.content,
                timestamp = entity.timestamp,
                metadata = MessageMetadata(
                    generatedApkPath = entity.generatedApkPath,
                    appName = entity.appName
                )
            )
        }
    }

    private fun generatePackageName(appName: String): String {
        val sanitized = appName.lowercase()
            .replace(Regex("[^a-z0-9]"), "")
            .take(20)
        return "com.aiher.generated.$sanitized"
    }

    private fun getDefaultSystemPrompt(): String {
        return """
你是一个专业的Android应用开发助手。你可以帮助用户：
1. 分析应用需求并给出建议
2. 生成Android应用代码
3. 解释Android开发概念
4. 解决开发中遇到的问题
5. 优化应用性能和用户体验

请用中文回答用户的问题，提供专业、详细的建议。
        """.trimIndent()
    }

    private fun getAppGeneratorSystemPrompt(): String {
        return """
你是一个Android应用生成器。根据用户的需求描述，生成完整的Android应用。你需要：

1. 分析用户需求，确定应用的功能和界面
2. 生成完整的Android项目代码，包括：
   - MainActivity
   - 布局文件
   - 必要的资源文件
   - AndroidManifest配置
3. 代码应该可以直接编译运行
4. 使用Material Design设计规范
5. 包含必要的错误处理和用户交互

请输出完整的代码，并说明每个文件的作用。
        """.trimIndent()
    }

    private fun buildAppGenerationPrompt(appName: String, requirements: String): String {
        return """
请帮我生成一个名为"$appName"的Android应用，需求如下：

$requirements

请生成完整的项目代码，可以直接编译运行。
        """.trimIndent()
    }
}