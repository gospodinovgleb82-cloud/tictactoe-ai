// GameController.kt — Участник 3 (Стас)
package com.team.tictactoe

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameController(
    private val boardSize: Int = 3,
    private val difficulty: Difficulty = Difficulty.HARD,
    private val onStateChanged: (GameState) -> Unit
) {
    private var state = GameState(
        board = List(boardSize * boardSize) { null },
        boardSize = boardSize,
        difficulty = difficulty
    )

    // Счётчик статистики
    var scoreHuman = 0
        private set
    var scoreAI = 0
        private set
    var scoreDraw = 0
        private set

    fun startNewGame() {
        state = GameState(
            board = List(boardSize * boardSize) { null },
            boardSize = boardSize,
            difficulty = difficulty
        )
        onStateChanged(state)
    }

    fun onHumanMove(cellIndex: Int) {
        if (state.status != GameStatus.PLAYING) return
        if (state.currentTurn != Player.HUMAN) return
        if (cellIndex !in 0 until boardSize * boardSize) return  // ИСПРАВЛЕНО
        if (state.board[cellIndex] != null) return

        val newBoard = state.board.toMutableList().also { it[cellIndex] = Player.HUMAN }
        state = state.copy(board = newBoard, currentTurn = Player.AI)
        state = evaluateStatus(state)
        onStateChanged(state)

        if (state.status == GameStatus.PLAYING) makeAiMove()
        else updateScore(state.status)
    }

    private fun makeAiMove() {
        CoroutineScope(Dispatchers.IO).launch {
            // Задержка зависит от сложности — умный AI "думает" дольше
            val delayMs = when (difficulty) {
                Difficulty.EASY   -> 300L
                Difficulty.MEDIUM -> 500L
                Difficulty.HARD   -> 700L
            }
            delay(delayMs)

            val bestMove = MinimaxAI.getBestMove(state.board, boardSize, difficulty)
            val newBoard = state.board.toMutableList().also { it[bestMove] = Player.AI }

            withContext(Dispatchers.Main) {
                state = state.copy(board = newBoard, currentTurn = Player.HUMAN)
                state = evaluateStatus(state)
                if (state.status != GameStatus.PLAYING) updateScore(state.status)
                onStateChanged(state)
            }
        }
    }

    private fun evaluateStatus(currentState: GameState): GameState {
        val winner = GameRules.checkWinner(currentState.board, boardSize)
        val status = when {
            winner == Player.HUMAN -> GameStatus.HUMAN_WIN
            winner == Player.AI    -> GameStatus.AI_WIN
            GameRules.isDraw(currentState.board, boardSize) -> GameStatus.DRAW
            else -> GameStatus.PLAYING
        }
        return currentState.copy(status = status)
    }

    private fun updateScore(status: GameStatus) {
        when (status) {
            GameStatus.HUMAN_WIN -> scoreHuman++
            GameStatus.AI_WIN    -> scoreAI++
            GameStatus.DRAW      -> scoreDraw++
            else -> {}
        }
    }

    fun getScoreText(): String = "Вы: $scoreHuman  |  AI: $scoreAI  |  Ничья: $scoreDraw"
}