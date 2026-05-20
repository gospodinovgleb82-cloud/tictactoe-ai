package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.load(this)
        setContentView(R.layout.activity_result)

        val result      = intent.getStringExtra("RESULT") ?: "DRAW"
        val boardSize   = intent.getIntExtra("BOARD_SIZE", 3)
        val difficulty  = intent.getSerializableExtra("DIFFICULTY") as? Difficulty ?: Difficulty.HARD
        val scoreHuman  = intent.getIntExtra("SCORE_HUMAN", 0)
        val scoreAI     = intent.getIntExtra("SCORE_AI", 0)
        val scoreDraw   = intent.getIntExtra("SCORE_DRAW", 0)

        val tvEmoji    = findViewById<TextView>(R.id.tvEmoji)
        val tvResult   = findViewById<TextView>(R.id.tvResult)
        val tvSubtitle = findViewById<TextView>(R.id.tvSubtitle)
        val tvScore    = findViewById<TextView>(R.id.tvScore)
        val btnAgain   = findViewById<Button>(R.id.btnPlayAgain)
        val btnMenu    = findViewById<Button>(R.id.btnMainMenu)

        when (result) {
            "HUMAN_WIN" -> {
                tvEmoji.text   = "🎉"
                tvResult.text  = "Вы победили!"
                tvSubtitle.text = when (difficulty) {
                    Difficulty.EASY   -> "Хорошее начало!"
                    Difficulty.MEDIUM -> "Отличная игра!"
                    Difficulty.HARD   -> "Невероятно! Вы победили Minimax AI!"
                }
            }
            "AI_WIN" -> {
                tvEmoji.text   = "🤖"
                tvResult.text  = "AI победил!"
                tvSubtitle.text = when (difficulty) {
                    Difficulty.EASY   -> "Попробуй ещё раз"
                    Difficulty.MEDIUM -> "AI был удачлив сегодня"
                    Difficulty.HARD   -> "Minimax непобедим на Сложном"
                }
            }
            else -> {
                tvEmoji.text   = "🤝"
                tvResult.text  = "Ничья!"
                tvSubtitle.text = "Хорошая игра!"
            }
        }

        tvScore.text = "Вы $scoreHuman  —  AI $scoreAI  —  Ничья $scoreDraw"

        // Apply theme
        val root = findViewById<LinearLayout>(R.id.rootResult)
        root.setBackgroundColor(ThemeManager.bgColor)
        tvResult.setTextColor(ThemeManager.textPrimaryColor)
        tvSubtitle.setTextColor(ThemeManager.textMutedColor)
        tvScore.setTextColor(ThemeManager.accentLightColor)
        btnAgain.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.accentColor)
        btnMenu.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        btnMenu.setTextColor(ThemeManager.accentLightColor)

        // Animate entrance
        root.alpha = 0f
        root.animate().alpha(1f).setDuration(400).start()
        tvEmoji.scaleX = 0f
        tvEmoji.scaleY = 0f
        tvEmoji.animate().scaleX(1f).scaleY(1f).setDuration(500).setStartDelay(200)
            .setInterpolator(android.view.animation.OvershootInterpolator()).start()

        btnAgain.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("BOARD_SIZE", boardSize)
            intent.putExtra("DIFFICULTY", difficulty)
            startActivity(intent)
            finish()
        }
        btnMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
