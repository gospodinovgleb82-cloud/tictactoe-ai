package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        ThemeManager.load(this)
        setContentView(R.layout.activity_main)

        applyTheme()

        val root = findViewById<LinearLayout>(R.id.rootLayout)
        root.alpha = 0f
        root.animate().alpha(1f).setDuration(600).setStartDelay(100).start()

        val tvLogo = findViewById<TextView>(R.id.tvLogoSymbols)
        val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce_in)
        tvLogo.startAnimation(bounceAnim)

        findViewById<Button>(R.id.btnStart).setOnClickListener {
            startActivity(Intent(this, SelectActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        findViewById<Button>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        findViewById<Button>(R.id.btnExit).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        ThemeManager.load(this)
        applyTheme()
    }

    private fun applyTheme() {
        val root = findViewById<LinearLayout>(R.id.rootLayout) ?: return
        root.setBackgroundColor(ThemeManager.bgColor)

        findViewById<TextView>(R.id.tvLogoSymbols)?.setTextColor(ThemeManager.accentLightColor)
        findViewById<TextView>(R.id.tvAppTitle)?.setTextColor(ThemeManager.textPrimaryColor)
        findViewById<TextView>(R.id.tvSubtitle)?.setTextColor(ThemeManager.textMutedColor)
        findViewById<View>(R.id.divider)?.setBackgroundColor(ThemeManager.accentColor)
        findViewById<TextView>(R.id.tvTechLabel)?.setTextColor(ThemeManager.textMutedColor)

        val tintList = { color: Int -> android.content.res.ColorStateList.valueOf(color) }

        findViewById<Button>(R.id.btnStart)?.backgroundTintList = tintList(ThemeManager.accentColor)
        findViewById<Button>(R.id.btnSettings)?.apply {
            backgroundTintList = tintList(ThemeManager.surfaceColor)
            setTextColor(ThemeManager.accentLightColor)
        }
        findViewById<Button>(R.id.btnExit)?.apply {
            backgroundTintList = tintList(ThemeManager.surfaceColor)
            setTextColor(ThemeManager.textMutedColor)
        }
    }
}
