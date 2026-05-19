// ResultActivity.kt — Участник 2 (Евгений)
package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    Difficulty.HARD   -> "Minimax непобедим на Hard"
                }
            }
            else -> {
                tvEmoji.text   = "🤝"
                tvResult.text  = "Ничья!"
                tvSubtitle.text = "Хорошая игра!"
            }
        }

        tvScore.text = "Счёт:  Вы $scoreHuman  —  AI $scoreAI  —  Ничья $scoreDraw"

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