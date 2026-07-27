package com.aiher.app.ai

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiher.app.data.local.SettingsDataStore
import com.aiher.app.data.model.AIModel
import com.aiher.app.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AiSettingsActivity : ComponentActivity() {

    @Inject lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AIHerTheme {
                AiSettingsScreen(
                    settingsDataStore = settingsDataStore,
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiSettingsScreen(
    settingsDataStore: SettingsDataStore,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var apiKey by rememberSaveable { mutableStateOf("") }
    var baseUrl by rememberSaveable { mutableStateOf("https://api.openai.com") }
    var selectedModel by rememberSaveable { mutableStateOf(AIModel.GPT4) }
    var temperature by rememberSaveable { mutableStateOf(0.7f) }
    var maxTokens by rememberSaveable { mutableStateOf("4096") }
    var showModelDropdown by remember { mutableStateOf(false) }
    var showApiKey by remember { mutableStateOf(false) }
    var loaded by remember { mutableStateOf(false) }

    // 加载已保存的设置
    LaunchedEffect(Unit) {
        if (!loaded) {
            apiKey = settingsDataStore.apiKey.firstOrEmpty()
            baseUrl = settingsDataStore.baseUrl.firstOrEmpty().ifBlank { "https://api.openai.com" }
            val savedModel = settingsDataStore.modelName.firstOrEmpty()
            selectedModel = AIModel.values().find { it.modelId == savedModel } ?: AIModel.GPT4
            temperature = settingsDataStore.temperature.firstOrDefault(0.7f)
            maxTokens = settingsDataStore.maxTokens.firstOrDefault(4096).toString()
            loaded = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI 配置", color = TextOnPrimary, fontWeight = FontWeight.Bold) },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 说明卡片
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F0FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Info, null, tint = Purple500, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("配置说明", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "支持 OpenAI、Claude、Gemini 等兼容API。\n" +
                        "如果使用第三方代理，请填写对应的 Base URL。\n" +
                        "不填写API Key也可以使用内置的本地回复功能。",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 20.sp
                    )
                }
            }

            // API Key
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("API Key", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = apiKey,
                        onValueChange = { apiKey = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("输入 API Key（可选）") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        visualTransformation = if (!showApiKey) {
                            androidx.compose.ui.text.input.PasswordVisualTransformation()
                        } else {
                            androidx.compose.ui.text.input.VisualTransformation.None
                        },
                        trailingIcon = {
                            IconButton(onClick = { showApiKey = !showApiKey }) {
                                Icon(
                                    if (showApiKey) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }

            // 模型选择
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI 模型", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Box {
                        OutlinedTextField(
                            value = selectedModel.displayName,
                            onValueChange = {},
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                IconButton(onClick = { showModelDropdown = true }) {
                                    Icon(Icons.Filled.ArrowDropDown, null)
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = showModelDropdown,
                            onDismissRequest = { showModelDropdown = false }
                        ) {
                            AIModel.values().forEach { model ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(model.displayName, fontWeight = FontWeight.Medium)
                                            Text(model.modelId, fontSize = 12.sp, color = TextSecondary)
                                        }
                                    },
                                    onClick = {
                                        selectedModel = model
                                        showModelDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // API Base URL
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("API Base URL", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = baseUrl,
                        onValueChange = { baseUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("https://api.openai.com") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }

            // 参数设置
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLight),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("参数设置", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Temperature
                    Text("Temperature: ${"%.1f".format(temperature)}", fontSize = 14.sp)
                    Slider(
                        value = temperature,
                        onValueChange = { temperature = it },
                        valueRange = 0f..2f,
                        steps = 19,
                        colors = SliderDefaults.colors(
                            thumbColor = Purple500,
                            activeTrackColor = Purple500
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Max Tokens
                    OutlinedTextField(
                        value = maxTokens,
                        onValueChange = { maxTokens = it.filter { c -> c.isDigit() } },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Max Tokens") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true
                    )
                }
            }

            // 保存按钮
            Button(
                onClick = {
                    scope.launch {
                        settingsDataStore.saveApiKey(apiKey.trim())
                        settingsDataStore.saveBaseUrl(baseUrl.trim())
                        settingsDataStore.saveModelName(selectedModel.modelId)
                        settingsDataStore.saveTemperature(temperature)
                        settingsDataStore.saveMaxTokens(maxTokens.toIntOrNull() ?: 4096)
                        Toast.makeText(context, "配置已保存", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple500)
            ) {
                Icon(Icons.Filled.Save, null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("保存配置", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 辅助扩展函数 - 安全获取第一个值
suspend fun kotlinx.coroutines.flow.Flow<String>.firstOrEmpty(): String {
    return try { this.first() } catch (e: Exception) { "" }
}

suspend fun kotlinx.coroutines.flow.Flow<Float>.firstOrDefault(default: Float): Float {
    return try { this.first() } catch (e: Exception) { default }
}

suspend fun kotlinx.coroutines.flow.Flow<Int>.firstOrDefault(default: Int): Int {
    return try { this.first() } catch (e: Exception) { default }
}
