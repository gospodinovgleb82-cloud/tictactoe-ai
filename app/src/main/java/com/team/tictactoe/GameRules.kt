package com.team.tictactoe

object GameRules {

    fun getWinCombinations(size: Int): List<List<Int>> {
        val combos = mutableListOf<List<Int>>()
        for (row in 0 until size)
            combos.add((0 until size).map { row * size + it })
        for (col in 0 until size)
            combos.add((0 until size).map { it * size + col })
        combos.add((0 until size).map { it * size + it })
        combos.add((0 until size).map { it * size + (size - 1 - it) })
        return combos
    }

    fun checkWinner(board: List<Player?>, size: Int = 3): Player? {
        for (combo in getWinCombinations(size)) {
            val first = board[combo[0]] ?: continue
            if (combo.all { board[it] == first }) return first
        }
        return null
    }

    fun isDraw(board: List<Player?>, size: Int = 3): Boolean {
        return board.none { it == null } && checkWinner(board, size) == null
    }

    fun getAvailableMoves(board: List<Player?>): List<Int> {
        return board.indices.filter { board[it] == null }
    }

    fun getWinningCombo(board: List<Player?>, size: Int = 3): List<Int>? {
        for (combo in getWinCombinations(size)) {
            val first = board[combo[0]] ?: continue
            if (combo.all { board[it] == first }) return combo
        }
        return null
    }
}