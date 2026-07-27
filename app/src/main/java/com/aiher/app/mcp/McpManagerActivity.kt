package com.aiher.app.mcp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

data class McpServer(
    val name: String,
    val description: String,
    val status: McpStatus,
    val type: McpType
)

enum class McpStatus { CONNECTED, DISCONNECTED, ERROR }
enum class McpType(val label: String) { SERVER("MCP Server"), SKILL("Skill"), SERVICE("服务") }

@AndroidEntryPoint
class McpManagerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                McpManagerScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun McpManagerScreen(onBack: () -> Unit) {
    val servers = remember {
        listOf(
            McpServer("文件系统", "读写本地文件和目录", McpStatus.CONNECTED, McpType.SERVER),
            McpServer("网络搜索", "实时搜索互联网信息", McpStatus.CONNECTED, McpType.SERVER),
            McpServer("代码执行", "在沙箱中执行Python/JS代码", McpStatus.CONNECTED, McpType.SERVER),
            McpServer("数据库查询", "SQL数据库操作能力", McpStatus.DISCONNECTED, McpType.SERVER),
            McpServer("图片生成", "通过AI生成图片", McpStatus.CONNECTED, McpType.SKILL),
            McpServer("应用打包", "将生成的代码打包为APK", McpStatus.CONNECTED, McpType.SKILL),
            McpServer("代码分析", "分析代码质量和安全性", McpStatus.CONNECTED, McpType.SKILL),
            McpServer("模板生成", "从模板快速创建项目", McpStatus.CONNECTED, McpType.SKILL),
            McpServer("定位服务", "获取设备GPS位置", McpStatus.CONNECTED, McpType.SERVICE),
            McpServer("剪贴板", "读写系统剪贴板", McpStatus.CONNECTED, McpType.SERVICE),
            McpServer("通知服务", "发送系统通知", McpStatus.CONNECTED, McpType.SERVICE),
            McpServer("悬浮窗", "在其他应用上方显示内容", McpStatus.DISCONNECTED, McpType.SERVICE),
        )
    }
    var selectedType by remember { mutableStateOf(McpType.SERVER) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MCP / Skill", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "返回", tint = TextOnPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Add, "添加", tint = TextOnPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple500)
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // 类型筛选
            TabRow(
                selectedTabIndex = McpType.values().indexOf(selectedType),
                containerColor = BackgroundLight
            ) {
                McpType.values().forEach { type ->
                    Tab(
                        selected = selectedType == type,
                        onClick = { selectedType = type },
                        text = { Text(type.label, color = if (selectedType == type) Purple500 else TextSecondary) }
                    )
                }
            }

            val filtered = servers.filter { it.type == selectedType }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filtered) { server ->
                    McpServerCard(server)
                }
            }
        }
    }
}

@Composable
fun McpServerCard(server: McpServer) {
    var enabled by remember { mutableStateOf(server.status == McpStatus.CONNECTED) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (server.type) {
                            McpType.SERVER -> Purple200
                            McpType.SKILL -> Color(0xFFE8F5E9)
                            McpType.SERVICE -> Color(0xFFE3F2FD)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (server.type) {
                        McpType.SERVER -> Icons.Filled.Dns
                        McpType.SKILL -> Icons.Filled.AutoAwesome
                        McpType.SERVICE -> Icons.Filled.Build
                    },
                    null,
                    tint = Purple500,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(server.name, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                Text(server.description, fontSize = 12.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(androidx.compose.foundation.shape.CircleShape)
                            .background(
                                if (enabled) SuccessGreen else Color(0xFFB2BEC3)
                            )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        if (enabled) "已连接" else "未连接",
                        fontSize = 11.sp,
                        color = if (enabled) SuccessGreen else TextSecondary
                    )
                }
            }

            Switch(
                checked = enabled,
                onCheckedChange = { enabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Purple500,
                    checkedTrackColor = Purple200
                )
            )
        }
    }
}