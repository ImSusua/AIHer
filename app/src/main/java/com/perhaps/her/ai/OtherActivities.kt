package com.perhaps.her.ai

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class PlusUpgradeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, PlusUpgradeActivity::class.java)
    }
}

class FeatureStoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, FeatureStoreActivity::class.java)
    }
}

class AiPluginsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, AiPluginsActivity::class.java)
    }
}

class MacControlActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, MacControlActivity::class.java)
    }
}

class MacQrCaptureActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class RootVmFridaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, RootVmFridaActivity::class.java)
    }
}

class AiFileDiffEditorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, AiFileDiffEditorActivity::class.java)
    }
}

class FullScreenWebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    companion object {
        fun createIntent(context: Context) = Intent(context, FullScreenWebActivity::class.java)
    }
}

class StubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}

class HerBackgroundTaskService : android.app.Service() {
    override fun onBind(intent: Intent?) = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int) = START_STICKY
}
