package com.team.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class GameActivity : AppCompatActivity() {

    private lateinit var controller: GameController
    private lateinit var cells: List<Button>
    private lateinit var tvStatus: TextView
    private lateinit var btnNewGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvStatus  = findViewById(R.id.tvStatus)
        btnNewGame = findViewById(R.id.btnNewGame)

        cells = listOf(
            findViewById(R.id.cell0), findViewById(R.id.cell1), findViewById(R.id.cell2),
            findViewById(R.id.cell3), findViewById(R.id.cell4), findViewById(R.id.cell5),
            findViewById(R.id.cell6), findViewById(R.id.cell7), findViewById(R.id.cell8)
        )

        cells.forEachIndexed { index, button ->
            button.setOnClickListener { controller.onHumanMove(index) }
        }

        btnNewGame.setOnClickListener { controller.startNewGame() }

        controller = GameController { state -> updateUI(state) }
        controller.startNewGame()
    }

    private fun updateUI(state: GameState) {
        runOnUiThread {
            state.board.forEachIndexed { index, player ->
                cells[index].text = player?.symbol() ?: ""
                cells[index].isEnabled = player == null && state.status == GameStatus.PLAYING
            }

            tvStatus.text = when (state.status) {
                GameStatus.PLAYING   -> if (state.currentTurn == Player.HUMAN) "Ваш ход (X)" else "AI думает..."
                GameStatus.HUMAN_WIN -> "Вы победили!"
                GameStatus.AI_WIN    -> "AI победил!"
                GameStatus.DRAW      -> "Ничья!"
            }

            val winCombo = GameRules.getWinningCombo(state.board)
            if (winCombo != null) {
                val color = if (state.status == GameStatus.HUMAN_WIN)
                    0xFF4ade80.toInt() else 0xFFf87171.toInt()
                winCombo.forEach { cells[it].setBackgroundColor(color) }
            }
        }
    }
}