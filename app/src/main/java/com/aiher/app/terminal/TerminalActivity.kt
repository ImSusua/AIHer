package com.aiher.app.terminal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TerminalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                TerminalScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen(onBack: () -> Unit) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf(listOf("AIHer Terminal v1.0.0", "输入 help 查看可用命令", "")) }

    fun executeCommand(cmd: String) {
        val result = when {
            cmd.trim() == "help" -> listOf(
                "可用命令:",
                "  help        - 显示帮助",
                "  ls          - 列出文件",
                "  pwd         - 显示当前目录",
                "  date        - 显示当前时间",
                "  echo <msg>  - 输出消息",
                "  clear       - 清屏",
                "  ai <prompt> - 使用AI生成",
                "  build       - 构建当前项目",
                "  claude      - 启动Claude Code",
                "  opencode    - 启动OpenCode",
                "  exit        - 退出终端"
            )
            cmd.trim() == "ls" -> listOf(
                "AndroidManifest.xml  build.gradle.kts  settings.gradle.kts",
                "app/  gradle/  .gitignore  README.md"
            )
            cmd.trim() == "pwd" -> listOf("/home/aiher/projects/current")
            cmd.trim() == "date" -> listOf(java.util.Date().toString())
            cmd.trim().startsWith("echo ") -> listOf(cmd.substring(5))
            cmd.trim() == "clear" -> { output = listOf(); return }
            cmd.trim().startsWith("ai ") -> listOf(
                "AI: 正在分析需求...",
                "AI: 生成项目结构...",
                "AI: 创建 MainActivity.kt",
                "AI: 创建布局文件",
                "AI: 配置 Gradle 依赖",
                "AI: 生成完成 ✓"
            )
            cmd.trim() == "build" -> listOf(
                "> Task :app:preBuild UP-TO-DATE",
                "> Task :app:compileDebugKotlin",
                "> Task :app:processDebugManifest",
                "> Task :app:compileDebugSources",
                "> Task :app:packageDebug",
                "BUILD SUCCESSFUL in 12s",
                "APK: app/build/outputs/apk/debug/app-debug.apk"
            )
            cmd.trim() == "claude" -> listOf("Claude Code v1.0", "Ready. 描述你想做什么...")
            cmd.trim() == "opencode" -> listOf("OpenCode v1.0", "Ready. 输入你的需求...")
            cmd.trim() == "exit" -> { onBack(); return }
            cmd.isBlank() -> listOf("")
            else -> listOf("命令未找到: $cmd", "输入 help 查看可用命令")
        }
        output = output + "aiher@localhost:~\$ $cmd" + result + ""
        if (output.size > 500) {
            output = output.takeLast(300)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Terminal", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "返回", tint = TextOnPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple500)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1A1A2E))
        ) {
            // 终端输出
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                output.forEach { line ->
                    Text(
                        text = line,
                        color = if (line.startsWith("aiher@")) SuccessGreen else Color(0xFF00FF00),
                        fontSize = 13.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // 快捷命令栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                listOf("help", "ls", "build", "ai", "claude").forEach { cmd ->
                    AssistChip(
                        onClick = { executeCommand(cmd) },
                        label = { Text(cmd, fontSize = 11.sp, color = Purple500) }
                    )
                }
            }

            // 输入栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("$ ", color = SuccessGreen, fontFamily = FontFamily.Monospace, fontSize = 14.sp)
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF00FF00),
                        unfocusedTextColor = Color(0xFF00FF00),
                        cursorColor = Color(0xFF00FF00),
                        focusedContainerColor = Color(0xFF16213E),
                        unfocusedContainerColor = Color(0xFF16213E),
                        focusedBorderColor = Purple500,
                        unfocusedBorderColor = Color(0xFF2D2D44)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    onClick = {
                        if (input.isNotBlank()) {
                            executeCommand(input)
                            input = ""
                        }
                    },
                    modifier = Modifier.size(40.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = Purple500)
                ) {
                    Icon(Icons.Filled.Send, null, tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}