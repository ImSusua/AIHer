package com.aiher.app.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.data.model.AppStatus
import com.aiher.app.data.model.GeneratedApp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                ProjectDetailScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailScreen(onBack: () -> Unit) {
    // 模拟项目数据
    val apps = remember {
        listOf(
            GeneratedApp(
                id = "1",
                name = "智能计算器",
                description = "支持科学计算和单位换算",
                packageName = "com.aiher.calculator",
                status = AppStatus.COMPLETED
            ),
            GeneratedApp(
                id = "2",
                name = "番茄钟",
                description = "番茄工作法时间管理",
                packageName = "com.aiher.pomodoro",
                status = AppStatus.COMPLETED
            ),
            GeneratedApp(
                id = "3",
                name = "2048游戏",
                description = "经典数字游戏",
                packageName = "com.aiher.game2048",
                status = AppStatus.GENERATING
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的项目", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, "返回", tint = TextOnPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple500)
            )
        }
    ) { paddingValues ->
        if (apps.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.FolderOpen,
                        null,
                        tint = TextSecondary,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("还没有项目", fontSize = 18.sp, color = TextSecondary)
                    Text("在AI对话中描述你想要创建的应用", fontSize = 14.sp, color = TextSecondary)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(apps) { app ->
                    ProjectCard(app)
                }
            }
        }
    }
}

@Composable
fun ProjectCard(app: GeneratedApp) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Purple200, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Filled.Android,
                    null,
                    tint = Purple500,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(app.name, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(app.description, fontSize = 13.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(app.packageName, fontSize = 11.sp, color = TextSecondary)
            }

            // 状态
            when (app.status) {
                AppStatus.GENERATING -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = WarningOrange,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("生成中", fontSize = 12.sp, color = WarningOrange)
                    }
                }
                AppStatus.COMPLETED -> {
                    FilledIconButton(
                        onClick = {},
                        modifier = Modifier.size(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = SuccessGreen
                        )
                    ) {
                        Icon(Icons.Filled.InstallMobile, "安装", tint = TextOnPrimary, modifier = Modifier.size(20.dp))
                    }
                }
                AppStatus.FAILED -> {
                    Text("失败", fontSize = 12.sp, color = ErrorRed)
                }
                AppStatus.INSTALLED -> {
                    Icon(Icons.Filled.CheckCircle, null, tint = SuccessGreen, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}