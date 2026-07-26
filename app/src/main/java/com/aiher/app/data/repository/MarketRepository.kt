package com.aiher.app.data.repository

import com.aiher.app.data.model.AIModel
import com.aiher.app.data.model.AppCategory
import com.aiher.app.data.model.MarketApp
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MarketRepository @Inject constructor() {

    suspend fun getMarketApps(category: AppCategory = AppCategory.ALL): List<MarketApp> {
        // 模拟数据 - 实际应用中从API获取
        return getMockApps().filter {
            category == AppCategory.ALL || it.category == category
        }
    }

    suspend fun searchApps(query: String): List<MarketApp> {
        return getMockApps().filter {
            it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
    }

    suspend fun getSupportedModels(): List<AIModel> {
        return AIModel.values().toList()
    }

    private fun getMockApps(): List<MarketApp> {
        return listOf(
            MarketApp(
                id = "1",
                name = "智能计算器",
                description = "一款功能强大的计算器应用，支持科学计算、单位换算和历史记录",
                packageName = "com.aiher.calculator",
                category = AppCategory.TOOLS,
                rating = 4.5f,
                downloads = 1200,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "2",
                name = "习惯打卡",
                description = "帮助你养成好习惯的打卡应用，支持提醒、统计和分享",
                packageName = "com.aiher.habit",
                category = AppCategory.LIFE,
                rating = 4.8f,
                downloads = 3500,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "3",
                name = "像素画板",
                description = "简单易用的像素画创作工具，支持导出和分享",
                packageName = "com.aiher.pixelart",
                category = AppCategory.ENTERTAINMENT,
                rating = 4.2f,
                downloads = 890,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "4",
                name = "番茄钟",
                description = "基于番茄工作法的时间管理工具，提升工作效率",
                packageName = "com.aiher.pomodoro",
                category = AppCategory.PRODUCTIVITY,
                rating = 4.6f,
                downloads = 2100,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "5",
                name = "每日一句",
                description = "每天推送一句名言警句，支持收藏和分享",
                packageName = "com.aiher.dailyquote",
                category = AppCategory.LIFE,
                rating = 4.3f,
                downloads = 1500,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "6",
                name = "简易记账",
                description = "轻量级的个人记账应用，支持分类统计和预算管理",
                packageName = "com.aiher.expense",
                category = AppCategory.TOOLS,
                rating = 4.7f,
                downloads = 2800,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "7",
                name = "2048游戏",
                description = "经典2048数字游戏，支持排行榜和成就系统",
                packageName = "com.aiher.game2048",
                category = AppCategory.GAMES,
                rating = 4.4f,
                downloads = 5000,
                author = "AIHer社区"
            ),
            MarketApp(
                id = "8",
                name = "单词本",
                description = "英语单词学习工具，支持艾宾浩斯记忆曲线",
                packageName = "com.aiher.vocab",
                category = AppCategory.EDUCATION,
                rating = 4.9f,
                downloads = 4200,
                author = "AIHer社区"
            )
        )
    }
}