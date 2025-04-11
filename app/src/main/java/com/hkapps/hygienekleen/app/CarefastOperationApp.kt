package com.hkapps.hygienekleen.app

import android.app.Application
import android.content.ContextWrapper
import com.hkapps.hygienekleen.utils.CommonUtils.scheduleResetPreferences
import com.pixplicity.easyprefs.library.Prefs

class CarefastOperationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Prefs.Builder().setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        scheduleResetPreferences(this)
    }
}