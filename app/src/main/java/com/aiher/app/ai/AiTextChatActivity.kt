package com.aiher.app.ai

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.data.model.ChatMessage
import com.aiher.app.data.model.MessageRole
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AiTextChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                AiChatScreen(
                    onNavigateToSettings = {
                        startActivity(Intent(this, AiSettingsActivity::class.java))
                    },
                    onNavigateToUserSettings = {
                        startActivity(Intent(this, com.aiher.app.settings.UserSettingsActivity::class.java))
                    },
                    onNavigateToFeatureStore = {
                        startActivity(Intent(this, com.aiher.app.market.FeatureStoreActivity::class.java))
                    },
                    onNavigateToPlus = {
                        startActivity(Intent(this, com.aiher.app.settings.PlusUpgradeActivity::class.java))
                    },
                    onNavigateToMcp = {
                        startActivity(Intent(this, com.aiher.app.mcp.McpManagerActivity::class.java))
                    },
                    onNavigateToWebApp = {
                        startActivity(Intent(this, com.aiher.app.webapp.WebAppActivity::class.java))
                    },
                    onNavigateToTerminal = {
                        startActivity(Intent(this, com.aiher.app.terminal.TerminalActivity::class.java))
                    },
                    onNavigateToDesktop = {
                        startActivity(Intent(this, com.aiher.app.desktop.DesktopExtensionActivity::class.java))
                    },
                    onNavigateToProjects = {
                        startActivity(Intent(this, com.aiher.app.project.ProjectDetailActivity::class.java))
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatScreen(
    onNavigateToSettings: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateToFeatureStore: () -> Unit,
    onNavigateToPlus: () -> Unit,
    onNavigateToMcp: () -> Unit,
    onNavigateToWebApp: () -> Unit,
    onNavigateToTerminal: () -> Unit,
    onNavigateToDesktop: () -> Unit,
    onNavigateToProjects: () -> Unit
) {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var inputText by remember { mutableStateOf("") }
    var isGenerating by remember { mutableStateOf(false) }
    var showSideMenu by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // 初始欢迎消息
    LaunchedEffect(Unit) {
        messages = listOf(
            ChatMessage(
                role = MessageRole.ASSISTANT,
                content = "你好！我是 AIHer，你的AI应用开发助手。\n\n你可以直接描述你想要创建的应用，我会帮你生成完整的Android应用。\n\n例如：\n- \"帮我创建一个计算器应用\"\n- \"制作一个番茄钟应用\"\n- \"生成一个2048游戏\"\n\n你想创建什么应用？"
            )
        )
    }

    ModalNavigationDrawer(
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        drawerContent = {
            if (showSideMenu) {
                SideMenuContent(
                    onNavigateToSettings = onNavigateToSettings,
                    onNavigateToUserSettings = onNavigateToUserSettings,
                    onNavigateToFeatureStore = onNavigateToFeatureStore,
                    onNavigateToPlus = onNavigateToPlus,
                    onNavigateToMcp = onNavigateToMcp,
                    onNavigateToWebApp = onNavigateToWebApp,
                    onNavigateToTerminal = onNavigateToTerminal,
                    onNavigateToDesktop = onNavigateToDesktop,
                    onNavigateToProjects = onNavigateToProjects,
                    onClose = { showSideMenu = false }
                )
            }
        },
        gesturesEnabled = showSideMenu
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "AIHer",
                            fontWeight = FontWeight.Bold,
                            color = TextOnPrimary
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Purple500
                    ),
                    navigationIcon = {
                        IconButton(onClick = { showSideMenu = !showSideMenu }) {
                            Icon(
                                Icons.Filled.Menu,
                                contentDescription = "菜单",
                                tint = TextOnPrimary
                            )
                        }
                    },
                    actions = {
                        // 新建对话
                        IconButton(onClick = {
                            messages = listOf(
                                ChatMessage(
                                    role = MessageRole.ASSISTANT,
                                    content = "新对话已开始。你想创建什么应用？"
                                )
                            )
                        }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = "新建对话",
                                tint = TextOnPrimary
                            )
                        }
                    }
                )
            },
            containerColor = BackgroundLight
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 消息列表
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    state = listState,
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(messages) { message ->
                        ChatBubble(message = message)
                    }

                    if (isGenerating) {
                        item {
                            GeneratingIndicator()
                        }
                    }
                }

                // 输入区域
                ChatInputBar(
                    inputText = inputText,
                    onInputChange = { inputText = it },
                    onSend = {
                        if (inputText.isNotBlank()) {
                            val userMessage = ChatMessage(
                                role = MessageRole.USER,
                                content = inputText
                            )
                            messages = messages + userMessage
                            inputText = ""
                            isGenerating = true

                            scope.launch {
                                // 自动滚动到底部
                                listState.animateScrollToItem(messages.size)

                                // 模拟AI响应
                                kotlinx.coroutines.delay(2000)

                                val aiResponse = generateMockResponse(userMessage.content)
                                messages = messages + ChatMessage(
                                    role = MessageRole.ASSISTANT,
                                    content = aiResponse
                                )
                                isGenerating = false
                                listState.animateScrollToItem(messages.size)
                            }
                        }
                    },
                    isGenerating = isGenerating
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val isUser = message.role == MessageRole.USER
    val bubbleColor = if (isUser) ChatUserBubble else ChatAIBubble
    val textColor = if (isUser) TextOnPrimary else TextPrimary

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            // AI头像
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Purple200),
                contentAlignment = Alignment.Center
            ) {
                Text("AI", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Purple700)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Card(
            modifier = Modifier.widthIn(max = 280.dp),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = message.content,
                modifier = Modifier.padding(12.dp),
                color = textColor,
                fontSize = 15.sp,
                lineHeight = 22.sp
            )
        }

        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Purple500),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Person,
                    contentDescription = null,
                    tint = TextOnPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun GeneratingIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp),
            colors = CardDefaults.cardColors(containerColor = ChatAIBubble)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Purple500,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI 正在生成应用...",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ChatInputBar(
    inputText: String,
    onInputChange: (String) -> Unit,
    onSend: () -> Unit,
    isGenerating: Boolean
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = SurfaceLight
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("描述你想要创建的应用...", color = TextSecondary) },
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple500,
                    unfocusedBorderColor = Divider,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color(0xFFF5F5F5)
                ),
                maxLines = 4,
                singleLine = false
            )

            Spacer(modifier = Modifier.width(8.dp))

            FilledIconButton(
                onClick = onSend,
                enabled = inputText.isNotBlank() && !isGenerating,
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = Purple500,
                    disabledContainerColor = Purple200
                )
            ) {
                Icon(
                    Icons.Filled.Send,
                    contentDescription = "发送",
                    tint = TextOnPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun SideMenuContent(
    onNavigateToSettings: () -> Unit,
    onNavigateToUserSettings: () -> Unit,
    onNavigateToFeatureStore: () -> Unit,
    onNavigateToPlus: () -> Unit,
    onNavigateToMcp: () -> Unit,
    onNavigateToWebApp: () -> Unit,
    onNavigateToTerminal: () -> Unit,
    onNavigateToDesktop: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onClose: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier.width(300.dp),
        drawerContainerColor = SurfaceLight
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 用户信息 - Pro版
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            listOf(SuccessGreen, Color(0xFF00B894))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Person, null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("AIHer 用户", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Verified, null, tint = SuccessGreen, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pro 版 · 永久免费", fontSize = 13.sp, color = SuccessGreen, fontWeight = FontWeight.Medium)
                }
            }
        }

        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        Spacer(modifier = Modifier.height(8.dp))

        // 核心功能
        MenuItem(Icons.Outlined.Folder, "我的项目", "查看和安装生成的应用") { onNavigateToProjects(); onClose() }
        MenuItem(Icons.Outlined.Store, "功能商店", "浏览和安装社区应用") { onNavigateToFeatureStore(); onClose() }
        MenuItem(Icons.Outlined.Web, "Web App", "生成和预览Web应用") { onNavigateToWebApp(); onClose() }
        MenuItem(Icons.Outlined.Verified, "Pro 会员", "全部功能已解锁") { onNavigateToPlus(); onClose() }

        Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))

        // 高级功能
        MenuItem(Icons.Outlined.Dns, "MCP / Skill", "管理工具和技能") { onNavigateToMcp(); onClose() }
        MenuItem(Icons.Outlined.Terminal, "终端", "Shell 和 CLI 工具") { onNavigateToTerminal(); onClose() }
        MenuItem(Icons.Outlined.DesktopWindows, "桌面扩展", "连接Mac工作区") { onNavigateToDesktop(); onClose() }

        Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))

        // 设置
        MenuItem(Icons.Outlined.Settings, "AI 配置", "设置AI模型和参数") { onNavigateToSettings(); onClose() }
        MenuItem(Icons.Outlined.Person, "用户设置", "管理账户和偏好") { onNavigateToUserSettings(); onClose() }

        Divider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))

        MenuItem(Icons.Outlined.Info, "关于", "AIHer v1.0.0 · 免费") {}
    }
}

@Composable
private fun MenuItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Purple500,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Medium, fontSize = 15.sp)
            Text(subtitle, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

// ============ 模拟AI响应 ============

private fun generateMockResponse(userInput: String): String {
    val input = userInput.lowercase()

    return when {
        input.contains("计算器") || input.contains("calculator") -> """
好的！我来为你创建一个**智能计算器**应用。

**应用功能：**
- 基本四则运算（加减乘除）
- 科学计算功能（三角函数、对数）
- 历史记录查看
- 支持横竖屏切换
- Material Design 3 风格界面

**技术方案：**
- Kotlin + Jetpack Compose
- 使用 `exp4j` 表达式解析库
- Room 数据库存储历史记录

**生成进度：**
已生成项目结构 ✅
已创建 MainActivity ✅
已创建计算器UI ✅
已实现计算逻辑 ✅
已添加历史记录功能 ✅

**应用已生成完成！** 你可以在"我的项目"中查看和安装。需要我调整什么功能吗？
        """.trimIndent()

        input.contains("番茄") || input.contains("番茄钟") || input.contains("pomodoro") -> """
好的！我来为你创建一个**番茄钟**时间管理应用。

**应用功能：**
- 25分钟专注 / 5分钟休息循环
- 自定义工作时长和休息时长
- 任务列表管理
- 专注统计和日报
- 通知提醒和白噪音
- 番茄成就徽章系统

**技术方案：**
- Kotlin + Jetpack Compose
- Foreground Service 后台计时
- Room 数据库存储记录
- MPAndroidChart 统计图表

**生成进度：**
已生成项目结构 ✅
已创建计时器核心逻辑 ✅
已创建UI界面 ✅
已添加通知提醒 ✅
已实现统计图表 ✅

**应用已生成完成！** 你可以在"我的项目"中查看和安装。需要调整什么功能吗？
        """.trimIndent()

        input.contains("2048") || input.contains("游戏") -> """
好的！我来为你创建一个**2048数字游戏**。

**应用功能：**
- 经典2048玩法（滑动合并数字）
- 支持触摸滑动和键盘操作
- 分数和历史最高分记录
- 撤销上一步操作
- 游戏结束判定和重新开始
- 动画效果（合并、出现）

**技术方案：**
- Kotlin + Jetpack Compose
- 自定义手势处理
- DataStore 持久化最高分
- Lottie 动画

**生成进度：**
已生成项目结构 ✅
已创建游戏逻辑 ✅
已创建UI界面 ✅
已实现手势控制 ✅
已添加动画效果 ✅

**应用已生成完成！** 你可以在"我的项目"中查看和安装。需要调整什么功能吗？
        """.trimIndent()

        input.contains("记账") || input.contains("账单") -> """
好的！我来为你创建一个**简易记账**应用。

**应用功能：**
- 收入/支出快速记录
- 分类管理（餐饮、交通、购物等）
- 月度/年度统计图表
- 预算设置和超支提醒
- 数据导出（CSV格式）
- 多账户管理

**技术方案：**
- Kotlin + Jetpack Compose
- Room 数据库
- MPAndroidChart 图表
- Material Design 3

**生成进度：**
已生成项目结构 ✅
已创建数据库模型 ✅
已创建UI界面 ✅
已实现统计图表 ✅
已添加导出功能 ✅

**应用已生成完成！** 你可以在"我的项目"中查看和安装。需要调整什么功能吗？
        """.trimIndent()

        else -> """
好的！我已经理解了你的需求。让我为你生成对应的应用。

**技术方案：**
- Kotlin + Jetpack Compose
- MVVM 架构
- Room 数据库
- Material Design 3 设计规范

**生成进度：**
已生成项目结构 ✅
已创建核心逻辑 ✅
已创建UI界面 ✅
已配置权限和依赖 ✅

**应用已生成完成！** 你可以在"我的项目"中查看和安装。

如果你需要更具体的功能，可以详细描述，比如：
- "创建一个带历史记录的计算器"
- "制作一个支持暗色主题的番茄钟"
- "生成一个带排行榜的2048游戏"
        """.trimIndent()
    }
}