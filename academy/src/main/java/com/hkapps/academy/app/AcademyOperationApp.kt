package com.hkapps.academy.app

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs

class AcademyOperationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.Builder().setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }
}