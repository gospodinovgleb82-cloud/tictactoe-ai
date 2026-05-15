package com.team.tictactoe

object GameDebug {

    fun printTestResults() {
        testWinnerRow()
        testWinnerDiagonal()
        testDraw()
        testAvailableMoves()
        println("Все тесты пройдены!")
    }

    private fun testWinnerRow() {
        val board = mutableListOf<Player?>(
            Player.HUMAN, Player.HUMAN, Player.HUMAN,
            null, Player.AI, null,
            null, null, Player.AI
        )
        val winner = GameRules.checkWinner(board)
        assert(winner == Player.HUMAN) { "Ошибка: победитель должен быть HUMAN" }
        println("testWinnerRow: OK")
    }

    private fun testWinnerDiagonal() {
        val board = mutableListOf<Player?>(
            Player.AI, Player.HUMAN, Player.HUMAN,
            null, Player.AI, null,
            Player.HUMAN, null, Player.AI
        )
        val winner = GameRules.checkWinner(board)
        assert(winner == Player.AI) { "Ошибка: победитель должен быть AI" }
        println("testWinnerDiagonal: OK")
    }

    private fun testDraw() {
        val board = listOf(
            Player.HUMAN, Player.AI,   Player.HUMAN,
            Player.HUMAN, Player.HUMAN, Player.AI,
            Player.AI,   Player.HUMAN, Player.AI
        )
        assert(GameRules.isDraw(board)) { "Ошибка: должна быть ничья" }
        println("testDraw: OK")
    }

    private fun testAvailableMoves() {
        val board = mutableListOf<Player?>(
            Player.HUMAN, null, Player.AI,
            null, Player.HUMAN, null,
            null, null, Player.AI
        )
        val moves = GameRules.getAvailableMoves(board)
        assert(moves == listOf(1, 3, 5, 6, 7)) { "Ошибка: неверные доступные ходы" }
        println("testAvailableMoves: OK")
    }
}