package com.perhaps.her.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * AiSettingsActivity - AI Configuration Screen
 */
class AiSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set settings UI
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AiSettingsActivity::class.java)
        }
    }
}
