package com.team.tictactoe

import android.content.Context
import android.graphics.Color

enum class AppTheme(val label: String) {
    DARK("Тёмная"),
    LIGHT("Светлая"),
    RED("Красная"),
    CYAN("Циановая")
}

object ThemeManager {
    private const val PREF_NAME = "ttt_prefs"
    private const val KEY_THEME = "app_theme"

    var currentTheme: AppTheme = AppTheme.DARK

    fun load(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val name = prefs.getString(KEY_THEME, AppTheme.DARK.name) ?: AppTheme.DARK.name
        currentTheme = runCatching { AppTheme.valueOf(name) }.getOrDefault(AppTheme.DARK)
    }

    fun save(context: Context, theme: AppTheme) {
        currentTheme = theme
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit().putString(KEY_THEME, theme.name).apply()
    }

    // Colors per theme
    val bgColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#07070f")
        AppTheme.LIGHT -> Color.parseColor("#f0f0ff")
        AppTheme.RED   -> Color.parseColor("#0f0707")
        AppTheme.CYAN  -> Color.parseColor("#07100f")
    }

    val surfaceColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#1a1a2e")
        AppTheme.LIGHT -> Color.parseColor("#ffffff")
        AppTheme.RED   -> Color.parseColor("#2e1a1a")
        AppTheme.CYAN  -> Color.parseColor("#0d2626")
    }

    val accentColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#7c3aed")
        AppTheme.LIGHT -> Color.parseColor("#7c3aed")
        AppTheme.RED   -> Color.parseColor("#dc2626")
        AppTheme.CYAN  -> Color.parseColor("#0891b2")
    }

    val accentLightColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#a78bfa")
        AppTheme.LIGHT -> Color.parseColor("#7c3aed")
        AppTheme.RED   -> Color.parseColor("#fca5a5")
        AppTheme.CYAN  -> Color.parseColor("#67e8f9")
    }

    val textPrimaryColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#e8e8f5")
        AppTheme.LIGHT -> Color.parseColor("#1a1a2e")
        AppTheme.RED   -> Color.parseColor("#f5e8e8")
        AppTheme.CYAN  -> Color.parseColor("#e8f5f5")
    }

    val textMutedColor get() = when (currentTheme) {
        AppTheme.DARK  -> Color.parseColor("#5a5a7a")
        AppTheme.LIGHT -> Color.parseColor("#6b7280")
        AppTheme.RED   -> Color.parseColor("#7a5a5a")
        AppTheme.CYAN  -> Color.parseColor("#3a6a6a")
    }
}
