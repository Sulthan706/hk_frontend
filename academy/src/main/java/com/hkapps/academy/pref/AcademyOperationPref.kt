package com.hkapps.academy.pref

import com.pixplicity.easyprefs.library.Prefs

class AcademyOperationPref {

    companion object {
        fun saveString(key: String, value: String) {
            Prefs.putString(key, value)
        }

        fun saveBoolean(key: String, value: Boolean) {
            Prefs.putBoolean(key, value)
        }

        fun saveInt(key: String, value: Int) {
            Prefs.putInt(key, value)
        }

        fun loadString(key: String, defaultValue: String): String {
            return Prefs.getString(key, defaultValue)
        }

        fun loadBoolean(key: String, defaultValue: Boolean): Boolean {
            return Prefs.getBoolean(key, defaultValue)
        }

        fun loadInt(key: String, defaultValue: Int): Int {
            return Prefs.getInt(key, defaultValue)
        }

        fun logout() {
            Prefs.clear()
        }
    }

}