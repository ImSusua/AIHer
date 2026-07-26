package com.aiher.app.data.model

import com.google.gson.annotations.SerializedName

// ============ AI对话相关模型 ============

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metadata: MessageMetadata? = null
)

enum class MessageRole {
    @SerializedName("user") USER,
    @SerializedName("assistant") ASSISTANT,
    @SerializedName("system") SYSTEM
}

data class MessageMetadata(
    val isGenerating: Boolean = false,
    val generatedApkPath: String? = null,
    val appName: String? = null,
    val appDescription: String? = null
)

// ============ AI模型配置 ============

data class AIModelConfig(
    val apiKey: String = "",
    val baseUrl: String = "https://api.openai.com",
    val modelName: String = "gpt-4",
    val temperature: Float = 0.7f,
    val maxTokens: Int = 4096
)

enum class AIModel(val displayName: String, val modelId: String) {
    GPT4("GPT-4", "gpt-4"),
    GPT4_TURBO("GPT-4 Turbo", "gpt-4-turbo-preview"),
    GPT35_TURBO("GPT-3.5 Turbo", "gpt-3.5-turbo"),
    CLAUDE35("Claude 3.5 Sonnet", "claude-3-5-sonnet-20241022"),
    CLAUDE3("Claude 3 Opus", "claude-3-opus-20240229"),
    GEMINI_PRO("Gemini Pro", "gemini-pro"),
    GEMINI_FLASH("Gemini Flash", "gemini-1.5-flash-latest")
}

// ============ API 请求/响应模型 ============

data class ChatCompletionRequest(
    val model: String,
    val messages: List<ChatCompletionMessage>,
    val temperature: Float = 0.7f,
    val max_tokens: Int = 4096
)

data class ChatCompletionMessage(
    val role: String,
    val content: String
)

data class ChatCompletionResponse(
    val id: String?,
    val choices: List<Choice>?,
    val error: APIError?
)

data class Choice(
    val message: ChatCompletionMessage?,
    val delta: DeltaContent?,
    val finish_reason: String?
)

data class DeltaContent(
    val content: String?
)

data class APIError(
    val message: String?,
    val type: String?,
    val code: String?
)

// ============ 应用生成结果 ============

data class GeneratedApp(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val packageName: String,
    val apkPath: String? = null,
    val iconUrl: String? = null,
    val sourceCode: String? = null,
    val status: AppStatus = AppStatus.GENERATING,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class AppStatus {
    GENERATING,
    COMPLETED,
    FAILED,
    INSTALLED
}

// ============ 应用市场模型 ============

data class MarketApp(
    val id: String,
    val name: String,
    val description: String,
    val packageName: String,
    val category: AppCategory,
    val iconUrl: String? = null,
    val screenshots: List<String> = emptyList(),
    val rating: Float = 0f,
    val downloads: Int = 0,
    val isFree: Boolean = true,
    val price: Double = 0.0,
    val author: String = "",
    val version: String = "1.0.0",
    val apkSize: Long = 0,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AppCategory(val displayName: String) {
    ALL("全部"),
    TOOLS("工具"),
    ENTERTAINMENT("娱乐"),
    LIFE("生活"),
    PRODUCTIVITY("效率"),
    SOCIAL("社交"),
    GAMES("游戏"),
    EDUCATION("教育")
}

// ============ 用户/项目模型 ============

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val avatarUrl: String? = null,
    val isPlus: Boolean = false,
    val plusExpireAt: Long? = null
)

data class Project(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val packageName: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val status: ProjectStatus = ProjectStatus.DRAFT,
    val messages: List<ChatMessage> = emptyList()
)

enum class ProjectStatus {
    DRAFT,
    GENERATING,
    COMPLETED,
    ARCHIVED
}