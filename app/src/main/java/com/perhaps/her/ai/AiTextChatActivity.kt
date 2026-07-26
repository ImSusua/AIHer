package com.perhaps.her.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.perhaps.her.R
import kotlinx.coroutines.launch

/**
 * AiTextChatActivity - Main AI Chat Interface
 * 
 * Core Features:
 * - Real-time chat with AI companion
 * - Message history display
 * - Accessibility integration
 * - Plugin system integration
 */
class AiTextChatActivity : AppCompatActivity() {

    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var chatScrollView: ScrollView
    private lateinit var chatTextView: TextView

    private val messageHistory = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_text_chat)
        
        initViews()
        setupListeners()
        loadChatHistory()
    }

    private fun initViews() {
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        chatScrollView = findViewById(R.id.chatScrollView)
        chatTextView = findViewById(R.id.chatTextView)
    }

    private fun setupListeners() {
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessage(message: String) {
        // Add user message
        messageHistory.add(ChatMessage(message, MessageRole.USER))
        appendToChat("我: $message\n\n", isUser = true)

        // Send to AI
        lifecycleScope.launch {
            getAIResponse(message)?.let { response ->
                messageHistory.add(ChatMessage(response, MessageRole.ASSISTANT))
                appendToChat("AI: $response\n\n", isUser = false)
            }
        }
    }

    private fun getAIResponse(userMessage: String): String? {
        // AI response logic - integrate with AI backend
        return when {
            userMessage.contains("你好", ignoreCase = true) -> "你好！有什么我可以帮助你的吗？"
            userMessage.contains("帮助", ignoreCase = true) -> "我可以帮你完成很多任务，比如查看手机状态、管理文件、搜索信息等。"
            else -> "我明白了，让我来处理这个问题。"
        }
    }

    private fun appendToChat(text: String, isUser: Boolean) {
        runOnUiThread {
            val color = if (isUser) "#2196F3" else "#4CAF50"
            chatTextView.append(text)
            chatScrollView.post {
                chatScrollView.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    private fun loadChatHistory() {
        // Load previous chat history from storage
    }

    private fun saveChatHistory() {
        // Save chat history to local storage
    }

    override fun onDestroy() {
        super.onDestroy()
        saveChatHistory()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AiTextChatActivity::class.java)
        }
    }
}

data class ChatMessage(
    val content: String,
    val role: MessageRole,
    val timestamp: Long = System.currentTimeMillis()
)

enum class MessageRole {
    USER,
    ASSISTANT,
    SYSTEM
}
