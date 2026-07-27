package com.aiher.app.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "aiher_settings")

class SettingsDataStore(private val context: Context) {

    companion object {
        private val KEY_API_KEY = stringPreferencesKey("api_key")
        private val KEY_BASE_URL = stringPreferencesKey("base_url")
        private val KEY_MODEL_NAME = stringPreferencesKey("model_name")
        private val KEY_TEMPERATURE = floatPreferencesKey("temperature")
        private val KEY_MAX_TOKENS = intPreferencesKey("max_tokens")
        private val KEY_IS_PLUS = booleanPreferencesKey("is_plus")
        private val KEY_THEME_MODE = intPreferencesKey("theme_mode")
        private val KEY_AUTO_INSTALL = booleanPreferencesKey("auto_install")
    }

    // AI配置
    val apiKey: Flow<String> = context.dataStore.data.map { it[KEY_API_KEY] ?: "" }
    val baseUrl: Flow<String> = context.dataStore.data.map { it[KEY_BASE_URL] ?: "https://api.openai.com" }
    val modelName: Flow<String> = context.dataStore.data.map { it[KEY_MODEL_NAME] ?: "gpt-4" }
    val temperature: Flow<Float> = context.dataStore.data.map { it[KEY_TEMPERATURE] ?: 0.7f }
    val maxTokens: Flow<Int> = context.dataStore.data.map { it[KEY_MAX_TOKENS] ?: 4096 }

    // 用户状态
    val isPlus: Flow<Boolean> = context.dataStore.data.map { it[KEY_IS_PLUS] ?: true }

    // 应用设置
    val themeMode: Flow<Int> = context.dataStore.data.map { it[KEY_THEME_MODE] ?: 0 }
    val autoInstall: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTO_INSTALL] ?: false }

    // 保存方法
    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { it[KEY_API_KEY] = key }
    }

    suspend fun saveBaseUrl(url: String) {
        context.dataStore.edit { it[KEY_BASE_URL] = url }
    }

    suspend fun saveModelName(name: String) {
        context.dataStore.edit { it[KEY_MODEL_NAME] = name }
    }

    suspend fun saveTemperature(temp: Float) {
        context.dataStore.edit { it[KEY_TEMPERATURE] = temp }
    }

    suspend fun saveMaxTokens(tokens: Int) {
        context.dataStore.edit { it[KEY_MAX_TOKENS] = tokens }
    }

    suspend fun setThemeMode(mode: Int) {
        context.dataStore.edit { it[KEY_THEME_MODE] = mode }
    }

    suspend fun setAutoInstall(enabled: Boolean) {
        context.dataStore.edit { it[KEY_AUTO_INSTALL] = enabled }
    }
}