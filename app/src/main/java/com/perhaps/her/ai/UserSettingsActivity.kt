package com.perhaps.her.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * UserSettingsActivity - User Preferences Screen
 */
class UserSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set user settings UI
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, UserSettingsActivity::class.java)
        }
    }
}
