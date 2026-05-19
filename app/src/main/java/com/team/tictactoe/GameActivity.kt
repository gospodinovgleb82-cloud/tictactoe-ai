// GameActivity.kt — Участник 2 (Евгений)
package com.team.tictactoe

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var controller: GameController
    private lateinit var cells: List<Button>
    private lateinit var tvStatus: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvDifficulty: TextView
    private lateinit var btnNewGame: Button
    private lateinit var gridBoard: GridLayout
    private var boardSize = 3
    private var difficulty = Difficulty.HARD

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        boardSize  = intent.getIntExtra("BOARD_SIZE", 3)
        difficulty = intent.getSerializableExtra("DIFFICULTY") as? Difficulty ?: Difficulty.HARD

        tvStatus     = findViewById(R.id.tvStatus)
        tvScore      = findViewById(R.id.tvScore)
        tvDifficulty = findViewById(R.id.tvDifficulty)
        btnNewGame   = findViewById(R.id.btnNewGame)
        gridBoard    = findViewById(R.id.gridBoard)

        tvDifficulty.text = "Сложность: ${difficulty.label()}  |  ${boardSize}×${boardSize}"

        setupBoard()
        btnNewGame.setOnClickListener { controller.startNewGame() }

        controller = GameController(boardSize, difficulty) { state -> updateUI(state) }
        controller.startNewGame()
    }

    private fun setupBoard() {
        gridBoard.removeAllViews()
        gridBoard.rowCount    = boardSize
        gridBoard.columnCount = boardSize

        val dp = resources.displayMetrics.density
        val cellSize = when (boardSize) { 3 -> 100; 4 -> 80; else -> 64 }

        val buttonList = mutableListOf<Button>()
        for (i in 0 until boardSize * boardSize) {
            val btn = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width  = (cellSize * dp).toInt()
                    height = (cellSize * dp).toInt()
                    setMargins(4, 4, 4, 4)
                }
                textSize = when (boardSize) { 3 -> 36f; 4 -> 28f; else -> 22f }
                setTextColor(0xFFa78bfa.toInt())
                setBackgroundColor(0xFF1a1a2e.toInt())
                setOnClickListener { controller.onHumanMove(i) }
            }
            gridBoard.addView(btn)
            buttonList.add(btn)
        }
        cells = buttonList
    }

    private fun updateUI(state: GameState) {
        runOnUiThread {
            // Обновляем ячейки
            state.board.forEachIndexed { index, player ->
                cells[index].text      = player?.symbol() ?: ""
                cells[index].isEnabled = player == null && state.status == GameStatus.PLAYING
                // Сбрасываем цвет если новая игра
                if (player == null) cells[index].setBackgroundColor(0xFF1a1a2e.toInt())
            }

            // Статус
            tvStatus.text = when (state.status) {
                GameStatus.PLAYING   -> if (state.currentTurn == Player.HUMAN)
                    "Ваш ход (X)" else "AI думает..."
                GameStatus.HUMAN_WIN -> "Вы победили! 🎉"
                GameStatus.AI_WIN    -> "AI победил! 🤖"
                GameStatus.DRAW      -> "Ничья! 🤝"
            }

            // Счёт
            tvScore.text = controller.getScoreText()

            // Подсветка победной комбинации
            val winCombo = GameRules.getWinningCombo(state.board, boardSize)
            if (winCombo != null) {
                val color = if (state.status == GameStatus.HUMAN_WIN)
                    0xFF4ade80.toInt() else 0xFFf87171.toInt()
                winCombo.forEach { cells[it].setBackgroundColor(color) }
            }

            // Переход на экран результата через 1.5 сек
            if (state.status != GameStatus.PLAYING) {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("RESULT", state.status.name)
                    intent.putExtra("BOARD_SIZE", boardSize)
                    intent.putExtra("DIFFICULTY", difficulty)
                    intent.putExtra("SCORE_HUMAN", controller.scoreHuman)
                    intent.putExtra("SCORE_AI", controller.scoreAI)
                    intent.putExtra("SCORE_DRAW", controller.scoreDraw)
                    startActivity(intent)
                }, 1500)
            }
        }
    }
}