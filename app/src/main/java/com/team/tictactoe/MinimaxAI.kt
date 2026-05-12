package com.team.tictactoe

object MinimaxAI {

    fun getBestMove(board: List<Player?>, size: Int = 3): Int {
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
        if (winner == Player.AI)          return 10 - depth
        if (winner == Player.HUMAN)       return depth - 10
        if (GameRules.isDraw(board, size)) return 0
        if (size == 4 && depth >= 4)      return 0

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