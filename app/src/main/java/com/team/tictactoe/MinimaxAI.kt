// MinimaxAI.kt — Участник 1 (Глеб)
// Алгоритм Minimax + Alpha-Beta + уровни сложности
package com.team.tictactoe

import kotlin.random.Random

object MinimaxAI {

    /**
     * Возвращает лучший ход для AI.
     * @param board  текущее поле
     * @param size   размер поля (3/4/5)
     * @param difficulty  уровень сложности
     */
    fun getBestMove(
        board: List<Player?>,
        size: Int = 3,
        difficulty: Difficulty = Difficulty.HARD
    ): Int {
        return when (difficulty) {
            Difficulty.EASY   -> getEasyMove(board, size)
            Difficulty.MEDIUM -> getMediumMove(board, size)
            Difficulty.HARD   -> getHardMove(board, size)
        }
    }

    // ── EASY: случайный ход ──────────────────────────────────────
    private fun getEasyMove(board: List<Player?>, size: Int): Int {
        val available = GameRules.getAvailableMoves(board)
        return available.random()
    }

    // ── MEDIUM: блокирует, иногда ошибается ─────────────────────
    private fun getMediumMove(board: List<Player?>, size: Int): Int {
        val available = GameRules.getAvailableMoves(board)

        // 30% шанс сделать случайный ход (ошибка AI)
        if (Random.nextInt(100) < 30) return available.random()

        // Проверяем свой победный ход
        for (index in available) {
            val newBoard = board.toMutableList().also { it[index] = Player.AI }
            if (GameRules.checkWinner(newBoard, size) == Player.AI) return index
        }

        // Блокируем победный ход человека
        for (index in available) {
            val newBoard = board.toMutableList().also { it[index] = Player.HUMAN }
            if (GameRules.checkWinner(newBoard, size) == Player.HUMAN) return index
        }

        // Берём центр если свободен
        val center = (size * size) / 2
        if (board[center] == null) return center

        // Случайный ход
        return available.random()
    }

    // ── HARD: полный Minimax + Alpha-Beta ───────────────────────
    private fun getHardMove(board: List<Player?>, size: Int): Int {
        if (size == 5) return getBestMoveHeuristic(board, size)

        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (index in GameRules.getAvailableMoves(board)) {
            val newBoard = board.toMutableList().also { it[index] = Player.AI }
            val score = minimax(newBoard, 0, false, Int.MIN_VALUE, Int.MAX_VALUE, size)
            if (score > bestScore) {
                bestScore = score
                bestMove = index
            }
        }
        return bestMove
    }

    private fun minimax(
        board: List<Player?>,
        depth: Int,
        isMaximizing: Boolean,
        alpha: Int,
        beta: Int,
        size: Int
    ): Int {
        val winner = GameRules.checkWinner(board, size)
        if (winner == Player.AI)           return 10 - depth
        if (winner == Player.HUMAN)        return depth - 10
        if (GameRules.isDraw(board, size)) return 0
        if (size == 4 && depth >= 4)       return 0

        val moves = GameRules.getAvailableMoves(board)
        var alphaLocal = alpha
        var betaLocal = beta

        return if (isMaximizing) {
            var best = Int.MIN_VALUE
            for (index in moves) {
                val newBoard = board.toMutableList().also { it[index] = Player.AI }
                val score = minimax(newBoard, depth + 1, false, alphaLocal, betaLocal, size)
                best = maxOf(best, score)
                alphaLocal = maxOf(alphaLocal, score)
                if (betaLocal <= alphaLocal) break
            }
            best
        } else {
            var best = Int.MAX_VALUE
            for (index in moves) {
                val newBoard = board.toMutableList().also { it[index] = Player.HUMAN }
                val score = minimax(newBoard, depth + 1, true, alphaLocal, betaLocal, size)
                best = minOf(best, score)
                betaLocal = minOf(betaLocal, score)
                if (betaLocal <= alphaLocal) break
            }
            best
        }
    }

    // Эвристика для 5×5
    private fun getBestMoveHeuristic(board: List<Player?>, size: Int): Int {
        val available = GameRules.getAvailableMoves(board)

        for (index in available) {
            val newBoard = board.toMutableList().also { it[index] = Player.AI }
            if (GameRules.checkWinner(newBoard, size) == Player.AI) return index
        }
        for (index in available) {
            val newBoard = board.toMutableList().also { it[index] = Player.HUMAN }
            if (GameRules.checkWinner(newBoard, size) == Player.HUMAN) return index
        }

        val center = (size * size) / 2
        if (board[center] == null) return center

        return available.random()
    }
}