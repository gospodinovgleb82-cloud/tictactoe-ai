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

        val result   = intent.getStringExtra("RESULT") ?: "DRAW"
        val tvEmoji  = findViewById<TextView>(R.id.tvEmoji)
        val tvResult = findViewById<TextView>(R.id.tvResult)
        val tvSub    = findViewById<TextView>(R.id.tvSubtitle)
        val btnAgain = findViewById<Button>(R.id.btnPlayAgain)
        val btnMenu  = findViewById<Button>(R.id.btnMainMenu)

        when (result) {
            "HUMAN_WIN" -> {
                tvEmoji.text  = "🎉"
                tvResult.text = "Вы победили!"
                tvSub.text    = "AI был побеждён!"
            }
            "AI_WIN" -> {
                tvEmoji.text  = "🤖"
                tvResult.text = "AI победил!"
                tvSub.text    = "Попробуй ещё раз"
            }
            else -> {
                tvEmoji.text  = "🤝"
                tvResult.text = "Ничья!"
                tvSub.text    = "Хорошая игра"
            }
        }

        btnAgain.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        btnMenu.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}