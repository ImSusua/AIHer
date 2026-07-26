package com.perhaps.her

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.perhaps.her.ai.AiTextChatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Navigate to AI Chat on launch
        startActivity(AiTextChatActivity.createIntent(this))
        finish()
    }
}
