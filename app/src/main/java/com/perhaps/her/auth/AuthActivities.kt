package com.perhaps.her.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * AuthGateActivity - OAuth Authentication Gate
 * 
 * Supports multiple OAuth providers:
 * - WeChat (微信)
 * - QQ
 * - Alipay (支付宝)
 */
class AuthGateActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Show login options
    }

    fun loginWithWeChat() {
        // Initiate WeChat OAuth
    }

    fun loginWithQQ() {
        // Initiate QQ OAuth
    }

    fun loginWithAlipay() {
        // Initiate Alipay OAuth
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthGateActivity::class.java)
        }
    }
}

/**
 * AuthCallbackActivity - OAuth Callback Handler
 */
class AuthCallbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleCallback()
    }

    private fun handleCallback() {
        // Handle OAuth callback and extract auth code
        val uri = intent.data
        uri?.let {
            val authCode = it.getQueryParameter("code")
            // Exchange code for token and authenticate user
        }
        finish()
    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, AuthCallbackActivity::class.java)
        }
    }
}
