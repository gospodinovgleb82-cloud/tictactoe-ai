package com.team.tictactoe

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameController(
    private val boardSize: Int = 3,
    private val onStateChanged: (GameState) -> Unit
) {
    private var state = GameState(
        board = List(boardSize * boardSize) { null },
        boardSize = boardSize
    )

    fun startNewGame() {
        state = GameState(
            board = List(boardSize * boardSize) { null },
            boardSize = boardSize
        )
        onStateChanged(state)
    }

    fun onHumanMove(cellIndex: Int) {
        if (state.status != GameStatus.PLAYING) return
        if (state.currentTurn != Player.HUMAN) return
        if (cellIndex !in 0..8) return
        if (state.board[cellIndex] != null) return

        val newBoard = state.board.toMutableList().also { it[cellIndex] = Player.HUMAN }
        state = state.copy(board = newBoard, currentTurn = Player.AI)
        state = evaluateStatus(state)
        onStateChanged(state)

        if (state.status == GameStatus.PLAYING) makeAiMove()
    }

    private fun makeAiMove() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
            val bestMove = MinimaxAI.getBestMove(state.board, boardSize)
            val newBoard = state.board.toMutableList().also { it[bestMove] = Player.AI }
            withContext(Dispatchers.Main) {
                state = state.copy(board = newBoard, currentTurn = Player.HUMAN)
                state = evaluateStatus(state)
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
}