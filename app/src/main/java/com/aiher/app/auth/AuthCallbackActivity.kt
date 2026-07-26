package com.aiher.app.auth

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthCallbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 处理OAuth回调
        val uri = intent?.data
        if (uri != null) {
            handleAuthCallback(uri.toString())
        }

        setContent {
            AIHerTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Purple500)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "正在验证...",
                            fontSize = 16.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }

    private fun handleAuthCallback(uri: String) {
        // 处理认证回调
        // 成功时导航到主界面
        startActivity(android.content.Intent(this, com.aiher.app.ai.AiTextChatActivity::class.java))
        finish()
    }
}