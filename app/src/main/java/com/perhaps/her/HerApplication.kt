package com.perhaps.her

import android.app.Application
import android.content.Context

class HerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: HerApplication
            private set

        fun getAppContext(): Context = instance.applicationContext
    }
}
