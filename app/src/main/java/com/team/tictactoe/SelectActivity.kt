// SelectActivity.kt — Участник 2 (Евгений)
// Экран выбора размера поля И сложности AI
package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SelectActivity : AppCompatActivity() {

    private var selectedSize = 3
    private var selectedDifficulty = Difficulty.HARD

    private lateinit var btnSizeLabels: List<Button>
    private lateinit var btnDiffLabels: List<Button>
    private lateinit var tvPreview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        tvPreview = findViewById(R.id.tvPreview)

        // Кнопки размера
        val btn3x3 = findViewById<Button>(R.id.btn3x3)
        val btn4x4 = findViewById<Button>(R.id.btn4x4)
        val btn5x5 = findViewById<Button>(R.id.btn5x5)
        btnSizeLabels = listOf(btn3x3, btn4x4, btn5x5)

        btn3x3.setOnClickListener { selectSize(3) }
        btn4x4.setOnClickListener { selectSize(4) }
        btn5x5.setOnClickListener { selectSize(5) }

        // Кнопки сложности
        val btnEasy   = findViewById<Button>(R.id.btnEasy)
        val btnMedium = findViewById<Button>(R.id.btnMedium)
        val btnHard   = findViewById<Button>(R.id.btnHard)
        btnDiffLabels = listOf(btnEasy, btnMedium, btnHard)

        btnEasy.setOnClickListener   { selectDifficulty(Difficulty.EASY) }
        btnMedium.setOnClickListener { selectDifficulty(Difficulty.MEDIUM) }
        btnHard.setOnClickListener   { selectDifficulty(Difficulty.HARD) }

        findViewById<Button>(R.id.btnPlay).setOnClickListener { startGame() }
        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }

        // Дефолтное выделение
        selectSize(3)
        selectDifficulty(Difficulty.HARD)
    }

    private fun selectSize(size: Int) {
        selectedSize = size
        btnSizeLabels.forEach { it.backgroundTintList =
            android.content.res.ColorStateList.valueOf(0xFF1a1a2e.toInt()) }
        val idx = when (size) { 3 -> 0; 4 -> 1; else -> 2 }
        btnSizeLabels[idx].backgroundTintList =
            android.content.res.ColorStateList.valueOf(0xFF7c3aed.toInt())
        updatePreview()
    }

    private fun selectDifficulty(d: Difficulty) {
        selectedDifficulty = d
        btnDiffLabels.forEach { it.backgroundTintList =
            android.content.res.ColorStateList.valueOf(0xFF1a1a2e.toInt()) }
        val idx = when (d) { Difficulty.EASY -> 0; Difficulty.MEDIUM -> 1; Difficulty.HARD -> 2 }
        btnDiffLabels[idx].backgroundTintList =
            android.content.res.ColorStateList.valueOf(0xFF7c3aed.toInt())
        updatePreview()
    }

    private fun updatePreview() {
        val diffText = when (selectedDifficulty) {
            Difficulty.EASY   -> "Можно победить легко"
            Difficulty.MEDIUM -> "AI иногда ошибается"
            Difficulty.HARD   -> "AI никогда не проигрывает"
        }
        tvPreview.text = "Поле ${selectedSize}×${selectedSize}  •  $diffText"
    }

    private fun startGame() {
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("BOARD_SIZE", selectedSize)
        intent.putExtra("DIFFICULTY", selectedDifficulty)
        startActivity(intent)
    }
}