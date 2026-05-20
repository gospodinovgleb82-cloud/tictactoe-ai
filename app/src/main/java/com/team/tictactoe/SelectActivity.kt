package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SelectActivity : AppCompatActivity() {

    private var selectedSize = 3
    private var selectedDifficulty = Difficulty.HARD

    private lateinit var sizeButtons: List<Button>
    private lateinit var diffButtons: List<Button>
    private lateinit var tvPreview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.load(this)
        setContentView(R.layout.activity_select)

        tvPreview = findViewById(R.id.tvPreview)

        val btn3x3 = findViewById<Button>(R.id.btn3x3)
        val btn4x4 = findViewById<Button>(R.id.btn4x4)
        val btn5x5 = findViewById<Button>(R.id.btn5x5)
        sizeButtons = listOf(btn3x3, btn4x4, btn5x5)

        btn3x3.setOnClickListener { selectSize(3) }
        btn4x4.setOnClickListener { selectSize(4) }
        btn5x5.setOnClickListener { selectSize(5) }

        val btnEasy   = findViewById<Button>(R.id.btnEasy)
        val btnMedium = findViewById<Button>(R.id.btnMedium)
        val btnHard   = findViewById<Button>(R.id.btnHard)
        diffButtons = listOf(btnEasy, btnMedium, btnHard)

        btnEasy.setOnClickListener   { selectDifficulty(Difficulty.EASY) }
        btnMedium.setOnClickListener { selectDifficulty(Difficulty.MEDIUM) }
        btnHard.setOnClickListener   { selectDifficulty(Difficulty.HARD) }

        findViewById<Button>(R.id.btnPlay).setOnClickListener { startGame() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        selectSize(3)
        selectDifficulty(Difficulty.HARD)
        applyTheme()
    }

    private fun applyTheme() {
        val root = findViewById<LinearLayout>(R.id.rootSelect)
        root.setBackgroundColor(ThemeManager.bgColor)

        findViewById<TextView>(R.id.tvSelectTitle).setTextColor(ThemeManager.textPrimaryColor)
        tvPreview.setTextColor(ThemeManager.accentLightColor)
        findViewById<TextView>(R.id.tvPreviewLabel).setTextColor(ThemeManager.textMutedColor)

        listOf<View>(
            findViewById(R.id.cardSize),
            findViewById(R.id.cardDiff),
            findViewById(R.id.cardPreview)
        ).forEach {
            it.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        }

        val btnPlay = findViewById<Button>(R.id.btnPlay)
        btnPlay.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.accentColor)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        btnBack.setTextColor(ThemeManager.textMutedColor)

        updateButtonColors()
    }

    private fun selectSize(size: Int) {
        selectedSize = size
        updateButtonColors()
        updatePreview()
    }

    private fun selectDifficulty(d: Difficulty) {
        selectedDifficulty = d
        updateButtonColors()
        updatePreview()
    }

    private fun updateButtonColors() {
        val sizeIdx = when (selectedSize) { 3 -> 0; 4 -> 1; else -> 2 }
        sizeButtons.forEachIndexed { i, btn ->
            btn.backgroundTintList = android.content.res.ColorStateList.valueOf(
                if (i == sizeIdx) ThemeManager.accentColor else ThemeManager.surfaceColor
            )
        }

        val diffIdx = when (selectedDifficulty) { Difficulty.EASY -> 0; Difficulty.MEDIUM -> 1; Difficulty.HARD -> 2 }
        diffButtons.forEachIndexed { i, btn ->
            btn.backgroundTintList = android.content.res.ColorStateList.valueOf(
                if (i == diffIdx) ThemeManager.accentColor else ThemeManager.surfaceColor
            )
        }
    }

    private fun updatePreview() {
        val diffText = when (selectedDifficulty) {
            Difficulty.EASY   -> "Случайные ходы"
            Difficulty.MEDIUM -> "AI иногда ошибается"
            Difficulty.HARD   -> "AI никогда не проигрывает"
        }
        tvPreview.text = "Поле ${selectedSize}×${selectedSize} · $diffText"
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("BOARD_SIZE", selectedSize)
        intent.putExtra("DIFFICULTY", selectedDifficulty)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }
}
