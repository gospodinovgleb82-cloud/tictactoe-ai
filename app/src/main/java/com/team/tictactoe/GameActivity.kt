package com.team.tictactoe

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    private lateinit var controller: GameController
    private lateinit var cells: List<Button>
    private lateinit var tvStatus: TextView
    private lateinit var tvScore: TextView
    private lateinit var tvDifficulty: TextView
    private lateinit var tvTaunt: TextView
    private lateinit var tvAdvPct: TextView
    private lateinit var advBarHuman: View
    private lateinit var panicBar: View
    private lateinit var btnNewGame: Button
    private lateinit var gridBoard: GridLayout
    private lateinit var tauntCard: LinearLayout

    private var boardSize = 3
    private var difficulty = Difficulty.HARD
    private var moveCount = 0
    private var isPanicking = false
    private var panicAnimator: ObjectAnimator? = null
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.load(this)
        setContentView(R.layout.activity_game)

        boardSize  = intent.getIntExtra("BOARD_SIZE", 3)
        difficulty = intent.getSerializableExtra("DIFFICULTY") as? Difficulty ?: Difficulty.HARD

        tvStatus     = findViewById(R.id.tvStatus)
        tvScore      = findViewById(R.id.tvScore)
        tvDifficulty = findViewById(R.id.tvDifficulty)
        tvTaunt      = findViewById(R.id.tvTaunt)
        tvAdvPct     = findViewById(R.id.tvAdvPct)
        advBarHuman  = findViewById(R.id.advBarHuman)
        panicBar     = findViewById(R.id.panicBar)
        btnNewGame   = findViewById(R.id.btnNewGame)
        gridBoard    = findViewById(R.id.gridBoard)
        tauntCard    = findViewById(R.id.tauntCard)

        val diffLabel = when (difficulty) {
            Difficulty.EASY   -> "Лёгкий"
            Difficulty.MEDIUM -> "Средний"
            Difficulty.HARD   -> "Сложный"
        }
        tvDifficulty.text = "$diffLabel · ${boardSize}×${boardSize}"

        setupBoard()
        applyTheme()

        btnNewGame.setOnClickListener {
            moveCount = 0
            stopPanic()
            controller.startNewGame()
            showTaunt(AiTaunt.getOpening())
        }

        controller = GameController(boardSize, difficulty) { state -> updateUI(state) }
        controller.startNewGame()
        showTaunt(AiTaunt.getOpening())
    }

    private fun applyTheme() {
        val root = findViewById<LinearLayout>(R.id.rootGame)
        root.setBackgroundColor(ThemeManager.bgColor)

        tvStatus.setTextColor(ThemeManager.textPrimaryColor)
        tvScore.setTextColor(ThemeManager.accentLightColor)
        tvDifficulty.setTextColor(ThemeManager.textMutedColor)
        tvTaunt.setTextColor(ThemeManager.accentLightColor)
        tvAdvPct.setTextColor(ThemeManager.textMutedColor)

        tauntCard.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.surfaceColor)
        btnNewGame.backgroundTintList = android.content.res.ColorStateList.valueOf(ThemeManager.accentColor)
    }

    private fun setupBoard() {
        gridBoard.removeAllViews()
        gridBoard.rowCount = boardSize
        gridBoard.columnCount = boardSize

        val dp = resources.displayMetrics.density
        val screenWidth = resources.displayMetrics.widthPixels
        val availableWidth = screenWidth - (32 * dp).toInt()
        val cellSize = (availableWidth / boardSize).coerceAtMost((96 * dp).toInt())

        val buttonList = mutableListOf<Button>()
        for (i in 0 until boardSize * boardSize) {
            val btn = Button(this).apply {
                layoutParams = GridLayout.LayoutParams().apply {
                    width  = cellSize
                    height = cellSize
                    setMargins(3, 3, 3, 3)
                }
                textSize = when (boardSize) { 3 -> 32f; 4 -> 26f; else -> 20f }
                setTextColor(ThemeManager.accentLightColor)
                setBackgroundColor(ThemeManager.surfaceColor)
                setOnClickListener {
                    controller.onHumanMove(i)
                }
            }
            gridBoard.addView(btn)
            buttonList.add(btn)
        }
        cells = buttonList
    }

    private fun updateUI(state: GameState) {
        runOnUiThread {
            state.board.forEachIndexed { index, player ->
                cells[index].text = player?.symbol() ?: ""
                cells[index].isEnabled = player == null && state.status == GameStatus.PLAYING
                if (player == null) cells[index].setBackgroundColor(ThemeManager.surfaceColor)
            }

            // Status text
            tvStatus.text = when (state.status) {
                GameStatus.PLAYING   -> if (state.currentTurn == Player.HUMAN) "Ваш ход (X)" else "AI думает…"
                GameStatus.HUMAN_WIN -> "Вы победили! 🎉"
                GameStatus.AI_WIN    -> "AI победил! 🤖"
                GameStatus.DRAW      -> "Ничья! 🤝"
            }

            tvScore.text = controller.getScoreText()

            // Advantage bar update
            val advantage = PositionEvaluator.evaluate(state.board, boardSize)
            updateAdvantageBar(advantage)

            // AI taunts based on game events
            if (state.status == GameStatus.PLAYING) {
                if (state.currentTurn == Player.AI) {
                    // Human just moved
                    moveCount++
                    val taunt = when {
                        moveCount > 6 -> AiTaunt.getLongGame()
                        advantage > 65 -> AiTaunt.getPlayerThreat()
                        advantage < 35 -> AiTaunt.getPlayerBlunder()
                        else -> AiTaunt.getPlayerMove().takeIf { it.isNotEmpty() } ?: return@runOnUiThread
                    }
                    showTaunt(taunt)
                } else {
                    // AI just moved
                    showTaunt(AiTaunt.getAiThinking())
                }

                // Panic mode: if AI is about to lose (human has high advantage)
                if (advantage > 75 && difficulty == Difficulty.HARD) {
                    triggerPanic()
                } else {
                    stopPanic()
                }
            }

            // Win combo highlight
            val winCombo = GameRules.getWinningCombo(state.board, boardSize)
            if (winCombo != null) {
                val color = if (state.status == GameStatus.HUMAN_WIN) 0xFF4ade80.toInt() else 0xFFf87171.toInt()
                winCombo.forEach { cells[it].setBackgroundColor(color) }
            }

            // End game taunt
            if (state.status != GameStatus.PLAYING) {
                stopPanic()
                val endTaunt = when (state.status) {
                    GameStatus.HUMAN_WIN -> AiTaunt.getPlayerWin()
                    GameStatus.AI_WIN    -> AiTaunt.getAiWin()
                    GameStatus.DRAW      -> AiTaunt.getDraw()
                    else -> ""
                }
                if (endTaunt.isNotEmpty()) showTaunt(endTaunt)

                handler.postDelayed({
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("RESULT", state.status.name)
                    intent.putExtra("BOARD_SIZE", boardSize)
                    intent.putExtra("DIFFICULTY", difficulty)
                    intent.putExtra("SCORE_HUMAN", controller.scoreHuman)
                    intent.putExtra("SCORE_AI", controller.scoreAI)
                    intent.putExtra("SCORE_DRAW", controller.scoreDraw)
                    startActivity(intent)
                }, 1800)
            }
        }
    }

    private fun updateAdvantageBar(humanPct: Float) {
        val barWidth = advBarHuman.parent as? FrameLayout ?: return
        barWidth.post {
            val totalWidth = barWidth.width
            val targetWidth = (totalWidth * (humanPct / 100f)).toInt()
            val anim = ValueAnimator.ofInt(advBarHuman.layoutParams.width, targetWidth)
            anim.duration = 300
            anim.addUpdateListener {
                advBarHuman.layoutParams.width = it.animatedValue as Int
                advBarHuman.requestLayout()
            }
            anim.start()
        }
        val humanInt = humanPct.toInt()
        val aiInt = 100 - humanInt
        tvAdvPct.text = "$humanInt% — $aiInt%"
    }

    private fun showTaunt(text: String) {
        if (text.isEmpty()) return
        tvTaunt.alpha = 0f
        tvTaunt.text = text
        tvTaunt.animate().alpha(1f).setDuration(300).start()
    }

    private fun triggerPanic() {
        if (isPanicking) return
        isPanicking = true
        panicBar.visibility = View.VISIBLE
        showTaunt(AiTaunt.getPanic())

        // Shake the game board
        val shakeAnim = AnimationUtils.loadAnimation(this, R.anim.shake)
        gridBoard.startAnimation(shakeAnim)

        // Blink the panic bar
        panicAnimator = ObjectAnimator.ofFloat(panicBar, "alpha", 1f, 0.1f).apply {
            duration = 300
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun stopPanic() {
        if (!isPanicking) return
        isPanicking = false
        panicAnimator?.cancel()
        panicBar.visibility = View.GONE
        gridBoard.clearAnimation()
    }
}
