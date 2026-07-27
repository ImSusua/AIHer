package com.aiher.app.desktop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DesktopExtensionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                DesktopExtensionScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesktopExtensionScreen(onBack: () -> Unit) {
    var isConnecting by remember { mutableStateOf(false) }
    var isConnected by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("桌面扩展", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 连接状态卡片
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                if (isConnected) listOf(SuccessGreen, Color(0xFF00B894))
                                else listOf(Purple500, Color(0xFF8B5CF6))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            if (isConnected) Icons.Filled.DesktopAccessDisabled else Icons.Filled.DesktopWindows,
                            null,
                            tint = Color.White,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            if (isConnected) "已连接" else "未连接",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            if (isConnected) "MacBook Pro" else "连接你的Mac扩展工作区",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // 连接按钮
            Button(
                onClick = {
                    isConnecting = true
                    kotlinx.coroutines.MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isConnecting = false
                        isConnected = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple500),
                enabled = !isConnecting && !isConnected
            ) {
                if (isConnecting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("正在连接...", color = Color.White)
                } else if (isConnected) {
                    Icon(Icons.Filled.CheckCircle, null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("已连接", color = Color.White)
                } else {
                    Icon(Icons.Filled.Link, null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("连接桌面设备", color = Color.White)
                }
            }

            // 功能介绍
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("桌面扩展能力", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    DesktopFeature("屏幕观察", "实时查看Mac屏幕内容")
                    DesktopFeature("桌面应用操作", "控制和操作桌面应用程序")
                    DesktopFeature("macOS 环境", "执行依赖macOS环境的步骤")
                    DesktopFeature("文件传输", "跨设备传输文件和项目")
                    DesktopFeature("跨端协作", "手机和Mac协同完成任务")
                }
            }

            // 连接步骤
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("如何连接", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    StepItem("1", "下载桌面辅助程序", "从 aibox.asia 下载 her-desktop-agent")
                    StepItem("2", "安装并运行", "在Mac上安装并启动辅助程序")
                    StepItem("3", "扫码配对", "用手机扫描Mac上的二维码")
                    StepItem("4", "开始使用", "连接成功后即可使用桌面扩展功能")
                }
            }
        }
    }
}

@Composable
private fun DesktopFeature(title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(Icons.Filled.CheckCircle, null, tint = SuccessGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(description, fontSize = 12.sp, color = TextSecondary)
        }
    }
}

@Composable
private fun StepItem(num: String, title: String, description: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Purple500),
            contentAlignment = Alignment.Center
        ) {
            Text(num, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(description, fontSize = 12.sp, color = TextSecondary)
        }
    }
}
