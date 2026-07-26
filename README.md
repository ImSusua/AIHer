# AIHer - AI驱动的Android应用生成器

仿照 **Her (aibox.asia)** 开发的Android应用，通过AI将自然语言需求直接转化为可安装的Android APK。

## 功能特性

### 核心功能
- **AI对话生成应用**: 输入自然语言描述，AI自动生成完整Android应用
- **应用市场**: 浏览和安装社区生成的各类应用
- **项目管理**: 管理生成的应用项目，支持查看和安装
- **AI模型配置**: 支持GPT-4、Claude、Gemini等多种AI模型

### 扩展功能
- 用户认证（登录/注册/游客模式）
- Plus会员升级（解锁更多功能）
- 后台服务
- 无障碍服务（AI辅助操作）
- 自定义AI参数（Temperature、Max Tokens等）

## 技术架构

| 层级 | 技术选型 |
|------|---------|
| 语言 | Kotlin |
| UI框架 | Jetpack Compose + Material Design 3 |
| 架构模式 | MVVM |
| 依赖注入 | Hilt |
| 网络请求 | Retrofit + OkHttp |
| 本地存储 | Room + DataStore |
| 异步处理 | Kotlin Coroutines |
| 图片加载 | Coil |

## 项目结构

```
AIHer/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── templates/          # 应用模板
│       │   └── meta-data/          # 元数据
│       ├── java/com/aiher/app/
│       │   ├── AIHerApplication.kt    # Application类
│       │   ├── auth/                  # 认证模块
│       │   │   ├── AuthGateActivity.kt
│       │   │   └── AuthCallbackActivity.kt
│       │   ├── ai/                    # AI核心模块
│       │   │   ├── AiTextChatActivity.kt
│       │   │   └── AiSettingsActivity.kt
│       │   ├── market/                # 应用市场
│       │   │   └── FeatureStoreActivity.kt
│       │   ├── settings/              # 设置模块
│       │   │   ├── UserSettingsActivity.kt
│       │   │   └── PlusUpgradeActivity.kt
│       │   ├── project/               # 项目管理
│       │   │   └── ProjectDetailActivity.kt
│       │   ├── service/               # 后台服务
│       │   │   ├── AIBackgroundService.kt
│       │   │   └── AIAccessibilityService.kt
│       │   ├── data/                  # 数据层
│       │   │   ├── model/Models.kt
│       │   │   ├── remote/ApiServices.kt
│       │   │   ├── local/
│       │   │   │   ├── AppDatabase.kt
│       │   │   │   └── SettingsDataStore.kt
│       │   │   └── repository/
│       │   │       ├── AIChatRepository.kt
│       │   │       ├── ProjectRepository.kt
│       │   │       └── MarketRepository.kt
│       │   ├── di/AppModule.kt        # 依赖注入
│       │   └── ui/theme/              # 主题
│       │       ├── Color.kt
│       │       └── Theme.kt
│       └── res/
│           ├── values/
│           │   ├── strings.xml
│           │   ├── colors.xml
│           │   └── themes.xml
│           └── xml/
│               ├── file_paths.xml
│               └── accessibility_service_config.xml
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## 构建运行

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高
- JDK 17
- Android SDK 34
- Gradle 8.5

### 构建步骤
1. 用 Android Studio 打开项目目录
2. 同步 Gradle 依赖
3. 连接 Android 设备或启动模拟器
4. 运行 `app` 模块

### 配置AI模型
1. 进入"AI 配置"页面
2. 填入 API Key
3. 选择模型（GPT-4/Claude/Gemini等）
4. 配置 API Base URL
5. 保存配置

## 与原Her的对比

| 功能 | 原Her | AIHer |
|------|-------|-------|
| AI对话 | ✅ | ✅ |
| 应用生成 | ✅ | ✅ |
| 应用市场 | ✅ | ✅ |
| 项目管理 | ✅ | ✅ |
| AI模型配置 | ✅ | ✅ |
| Plus会员 | ✅ | ✅ |
| 无障碍服务 | ✅ | ✅ |
| 后台服务 | ✅ | ✅ |
| MCP/Skill | ✅ | 🔜 |
| 桌面扩展 | ✅ | 🔜 |
| Web App生成 | ✅ | 🔜 |

## 开源协议

MIT License