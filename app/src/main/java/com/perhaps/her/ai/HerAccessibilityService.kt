package com.perhaps.her.ai

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent

/**
 * HerAccessibilityService - Provides accessibility features for AI interactions
 * 
 * This service enables:
 * - Screen content reading for AI context
 * - UI element interaction automation
 * - Screen state monitoring
 * - Text extraction from any screen
 */
class HerAccessibilityService : AccessibilityService() {

    private var serviceInfo: AccessibilityServiceInfo? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_ALL_EVENTS
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 100
        }
        serviceInfo?.let { service -> this.serviceInfo = service }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event?.let {
            // Handle accessibility events for AI processing
            // Content can be extracted and sent to AI for context
        }
    }

    override fun onInterrupt() {
        // Service interrupted
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private var instance: HerAccessibilityService? = null

        fun isRunning(): Boolean = instance != null

        fun getInstance(): HerAccessibilityService? = instance

        fun getWindowContent(): String? {
            return instance?.rootInActiveWindow?.let { root ->
                val sb = StringBuilder()
                extractContent(root, sb)
                sb.toString()
            }
        }

        private fun extractContent(node: android.view.View?, sb: StringBuilder) {
            node?.let {
                if (it.text?.isNotEmpty() == true) {
                    sb.append(it.text).append("\n")
                }
                it.children?.forEach { child ->
                    extractContent(child, sb)
                }
            }
        }
    }
}
