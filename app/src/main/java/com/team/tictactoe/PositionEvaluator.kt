package com.team.tictactoe

/**
 * Evaluates board position as a percentage advantage for the human player.
 * 50% = equal, >50% = human advantage, <50% = AI advantage
 */
object PositionEvaluator {

    fun evaluate(board: List<Player?>, size: Int): Float {
        val winner = GameRules.checkWinner(board, size)
        if (winner == Player.HUMAN) return 100f
        if (winner == Player.AI) return 0f
        if (GameRules.isDraw(board, size)) return 50f

        val empty = board.count { it == null }
        val total = size * size

        // If board is empty
        if (empty == total) return 50f

        var humanScore = 0f
        var aiScore = 0f

        for (combo in GameRules.getWinCombinations(size)) {
            val cells = combo.map { board[it] }
            val humanCount = cells.count { it == Player.HUMAN }
            val aiCount = cells.count { it == Player.AI }

            if (aiCount == 0 && humanCount > 0) {
                humanScore += when (humanCount) {
                    size - 1 -> 100f  // One away from winning
                    size - 2 -> 10f
                    else -> 2f
                }
            }
            if (humanCount == 0 && aiCount > 0) {
                aiScore += when (aiCount) {
                    size - 1 -> 80f   // AI one away (slightly less since AI plays after)
                    size - 2 -> 8f
                    else -> 1.5f
                }
            }
        }

        val total2 = humanScore + aiScore
        if (total2 == 0f) return 50f

        // Normalize to 0-100 range, clamp between 5 and 95
        val raw = (humanScore / total2) * 100f
        return raw.coerceIn(5f, 95f)
    }
}
