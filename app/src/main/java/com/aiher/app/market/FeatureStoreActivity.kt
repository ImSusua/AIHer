package com.aiher.app.market

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.data.model.AppCategory
import com.aiher.app.data.model.MarketApp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeatureStoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                FeatureStoreScreen(onBack = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeatureStoreScreen(onBack: () -> Unit) {
    var selectedCategory by remember { mutableStateOf(AppCategory.ALL) }
    var searchQuery by remember { mutableStateOf("") }

    // 模拟数据
    val allApps = remember {
        listOf(
            MarketApp("1", "智能计算器", "功能强大的科学计算器", "com.aiher.calculator", AppCategory.TOOLS, rating = 4.5f, downloads = 1200, author = "AIHer"),
            MarketApp("2", "习惯打卡", "养成好习惯的打卡应用", "com.aiher.habit", AppCategory.LIFE, rating = 4.8f, downloads = 3500, author = "AIHer"),
            MarketApp("3", "像素画板", "像素画创作工具", "com.aiher.pixelart", AppCategory.ENTERTAINMENT, rating = 4.2f, downloads = 890, author = "AIHer"),
            MarketApp("4", "番茄钟", "番茄工作法时间管理", "com.aiher.pomodoro", AppCategory.PRODUCTIVITY, rating = 4.6f, downloads = 2100, author = "AIHer"),
            MarketApp("5", "每日一句", "每日名言推送", "com.aiher.dailyquote", AppCategory.LIFE, rating = 4.3f, downloads = 1500, author = "AIHer"),
            MarketApp("6", "简易记账", "个人财务管理", "com.aiher.expense", AppCategory.TOOLS, rating = 4.7f, downloads = 2800, author = "AIHer"),
            MarketApp("7", "2048游戏", "经典数字游戏", "com.aiher.game2048", AppCategory.GAMES, rating = 4.4f, downloads = 5000, author = "AIHer"),
            MarketApp("8", "单词本", "英语学习工具", "com.aiher.vocab", AppCategory.EDUCATION, rating = 4.9f, downloads = 4200, author = "AIHer"),
        )
    }

    val filteredApps = remember(selectedCategory, searchQuery) {
        allApps.filter { app ->
            (selectedCategory == AppCategory.ALL || app.category == selectedCategory) &&
            (searchQuery.isEmpty() || app.name.contains(searchQuery, ignoreCase = true))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("功能商店", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
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
        ) {
            // 搜索栏
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = { Text("搜索应用...") },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Purple500,
                    focusedContainerColor = SurfaceLight
                )
            )

            // 分类标签
            ScrollableTabRow(
                selectedTabIndex = AppCategory.values().indexOf(selectedCategory),
                modifier = Modifier.fillMaxWidth(),
                containerColor = BackgroundLight,
                edgePadding = 16.dp,
                divider = {}
            ) {
                AppCategory.values().forEach { category ->
                    Tab(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        text = {
                            Text(
                                category.displayName,
                                color = if (selectedCategory == category) Purple500 else TextSecondary,
                                fontWeight = if (selectedCategory == category) FontWeight.SemiBold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // 应用列表
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredApps) { app ->
                    MarketAppCard(app)
                }
            }
        }
    }
}

@Composable
fun MarketAppCard(app: MarketApp) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {},
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 应用图标占位
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        when (app.category) {
                            AppCategory.TOOLS -> Color(0xFFE8F5E9)
                            AppCategory.LIFE -> Color(0xFFE3F2FD)
                            AppCategory.ENTERTAINMENT -> Color(0xFFFFF3E0)
                            AppCategory.GAMES -> Color(0xFFFCE4EC)
                            AppCategory.PRODUCTIVITY -> Color(0xFFF3E5F5)
                            AppCategory.EDUCATION -> Color(0xFFE0F7FA)
                            else -> Color(0xFFF5F5F5)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    app.name.take(1),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple500
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                app.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                app.description,
                fontSize = 12.sp,
                color = TextSecondary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(32.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Star,
                        null,
                        tint = WarningOrange,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        "${app.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    "${app.downloads} 下载",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }
    }
}