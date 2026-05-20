package com.team.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var themeButtons: List<Pair<AppTheme, Button>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.load(this)
        setContentView(R.layout.activity_settings)

        themeButtons = listOf(
            AppTheme.DARK  to findViewById(R.id.btnThemeDark),
            AppTheme.LIGHT to findViewById(R.id.btnThemeLight),
            AppTheme.RED   to findViewById(R.id.btnThemeRed),
            AppTheme.CYAN  to findViewById(R.id.btnThemeCyan)
        )

        themeButtons.forEach { (theme, btn) ->
            btn.setOnClickListener {
                ThemeManager.save(this, theme)
                updateSelection()
                applyTheme()
            }
        }

        findViewById<Button>(R.id.btnBackSettings).setOnClickListener { finish() }

        updateSelection()
        applyTheme()
    }

    private fun updateSelection() {
        themeButtons.forEach { (theme, btn) ->
            val isSelected = theme == ThemeManager.currentTheme
            btn.backgroundTintList = android.content.res.ColorStateList.valueOf(
                if (isSelected) ThemeManager.accentColor else ThemeManager.surfaceColor
            )
            btn.setTextColor(
                if (isSelected) 0xFFFFFFFF.toInt() else ThemeManager.textMutedColor
            )
        }
    }

    private fun applyTheme() {
        val root = findViewById<LinearLayout>(R.id.rootSettings)
        root.setBackgroundColor(ThemeManager.bgColor)

        listOf<TextView>(
            findViewById(R.id.tvSettingsTitle),
        ).forEach { it.setTextColor(ThemeManager.textPrimaryColor) }

        listOf<TextView>(
            findViewById(R.id.tvThemeLabel),
        ).forEach { it.setTextColor(ThemeManager.textMutedColor) }

        // Card backgrounds
        listOf<View>(
            findViewById(R.id.cardTheme),
        ).forEach {
            it.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        }

        val btnBack = findViewById<Button>(R.id.btnBackSettings)
        btnBack.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        btnBack.setTextColor(ThemeManager.textMutedColor)

        updateSelection()
    }
}
