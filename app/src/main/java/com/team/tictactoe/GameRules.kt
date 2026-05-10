package com.team.tictactoe

object GameRules {

    val WIN_COMBINATIONS = listOf(
        listOf(0, 1, 2),
        listOf(3, 4, 5),
        listOf(6, 7, 8),
        listOf(0, 3, 6),
        listOf(1, 4, 7),
        listOf(2, 5, 8),
        listOf(0, 4, 8),
        listOf(2, 4, 6)
    )

    fun checkWinner(board: List<Player?>): Player? {
        for (combo in WIN_COMBINATIONS) {
            val (a, b, c) = combo
            if (board[a] != null && board[a] == board[b] && board[b] == board[c]) {
                return board[a]
            }
        }
        return null
    }

    fun isDraw(board: List<Player?>): Boolean {
        return board.none { it == null } && checkWinner(board) == null
    }

    fun getAvailableMoves(board: List<Player?>): List<Int> {
        return board.indices.filter { board[it] == null }
    }

    fun getWinningCombo(board: List<Player?>): List<Int>? {
        for (combo in WIN_COMBINATIONS) {
            val (a, b, c) = combo
            if (board[a] != null && board[a] == board[b] && board[b] == board[c]) {
                return combo
            }
        }
        return null
    }
}