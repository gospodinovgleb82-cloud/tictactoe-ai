package com.team.tictactoe

object MinimaxAI {

    fun getBestMove(board: List<Player?>): Int {
        var bestScore = Int.MIN_VALUE
        var bestMove = -1

        for (index in GameRules.getAvailableMoves(board)) {
            val newBoard = board.toMutableList().also { it[index] = Player.AI }
            val score = minimax(
                board = newBoard,
                depth = 0,
                isMaximizing = false,
                alpha = Int.MIN_VALUE,
                beta = Int.MAX_VALUE
            )
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
        beta: Int
    ): Int {
        val winner = GameRules.checkWinner(board)
        if (winner == Player.AI)    return 10 - depth
        if (winner == Player.HUMAN) return depth - 10
        if (GameRules.isDraw(board)) return 0

        val availableMoves = GameRules.getAvailableMoves(board)
        var alphaLocal = alpha
        var betaLocal = beta

        return if (isMaximizing) {
            var bestScore = Int.MIN_VALUE
            for (index in availableMoves) {
                val newBoard = board.toMutableList().also { it[index] = Player.AI }
                val score = minimax(newBoard, depth + 1, false, alphaLocal, betaLocal)
                bestScore = maxOf(bestScore, score)
                alphaLocal = maxOf(alphaLocal, score)
                if (betaLocal <= alphaLocal) break
            }
            bestScore
        } else {
            var bestScore = Int.MAX_VALUE
            for (index in availableMoves) {
                val newBoard = board.toMutableList().also { it[index] = Player.HUMAN }
                val score = minimax(newBoard, depth + 1, true, alphaLocal, betaLocal)
                bestScore = minOf(bestScore, score)
                betaLocal = minOf(betaLocal, score)
                if (betaLocal <= alphaLocal) break
            }
            bestScore
        }
    }
}