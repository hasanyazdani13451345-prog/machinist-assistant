package com.machinist.assistant

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class MachinistApp : Application() {

    override fun onCreate() {
        super.onCreate()
        val prefs = prefs(this)

        // Default language: Persian (bilingual app, user can switch to English)
        if (!prefs.getBoolean(KEY_LANG_INIT, false)) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("fa"))
            prefs.edit().putBoolean(KEY_LANG_INIT, true).apply()
        }

        // Theme (dark industrial by default)
        when (prefs.getString(KEY_NIGHT, "dark")) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    companion object {
        const val KEY_LANG_INIT = "lang_initialized"
        const val KEY_NIGHT = "night_mode"

        fun prefs(context: Context) =
            context.getSharedPreferences("machinist_settings", Context.MODE_PRIVATE)
    }
}
