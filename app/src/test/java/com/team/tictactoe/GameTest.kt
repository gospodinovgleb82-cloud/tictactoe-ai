package com.team.tictactoe

import org.junit.Assert.*
import org.junit.Test

class GameRulesTest {

    @Test
    fun humanWinsOnTopRow() {
        val board = mutableListOf<Player?>(
            Player.HUMAN, Player.HUMAN, Player.HUMAN,
            null, Player.AI, null,
            null, null, Player.AI
        )
        assertEquals(Player.HUMAN, GameRules.checkWinner(board))
    }

    @Test
    fun aiWinsOnDiagonal() {
        val board = mutableListOf<Player?>(
            Player.AI, Player.HUMAN, Player.HUMAN,
            null, Player.AI, null,
            Player.HUMAN, null, Player.AI
        )
        assertEquals(Player.AI, GameRules.checkWinner(board))
    }

    @Test
    fun noWinnerOnEmptyBoard() {
        val board = List<Player?>(9) { null }
        assertNull(GameRules.checkWinner(board))
    }

    @Test
    fun drawDetectedCorrectly() {
        val board = listOf(
            Player.HUMAN, Player.AI,    Player.HUMAN,
            Player.HUMAN, Player.HUMAN, Player.AI,
            Player.AI,    Player.HUMAN, Player.AI
        )
        assertTrue(GameRules.isDraw(board))
    }

    @Test
    fun availableMovesCorrect() {
        val board = mutableListOf<Player?>(
            Player.HUMAN, null, Player.AI,
            null, Player.HUMAN, null,
            null, null, Player.AI
        )
        assertEquals(listOf(1, 3, 5, 6, 7), GameRules.getAvailableMoves(board))
    }

    @Test
    fun clickOnOccupiedCellIgnored() {
        val board = mutableListOf<Player?>(
            Player.HUMAN, null, null,
            null, null, null,
            null, null, null
        )
        // Ячейка 0 занята — доступные ходы не включают 0
        assertFalse(GameRules.getAvailableMoves(board).contains(0))
    }
}

class MinimaxAITest {

    @Test
    fun aiBlocksHumanFromWinning() {
        // Человек вот-вот выиграет: X X _
        val board = mutableListOf<Player?>(
            Player.HUMAN, Player.HUMAN, null,
            null, Player.AI, null,
            null, null, null
        )
        // AI должен заблокировать позицию 2
        assertEquals(2, MinimaxAI.getBestMove(board))
    }

    @Test
    fun aiTakesWinningMove() {
        // AI вот-вот выиграет по столбцу
        val board = mutableListOf<Player?>(
            Player.AI,   Player.HUMAN, Player.HUMAN,
            Player.AI,   Player.HUMAN, null,
            null,        null,         null
        )
        val move = MinimaxAI.getBestMove(board)
        val newBoard = board.toMutableList().also { it[move] = Player.AI }
        assertEquals(Player.AI, GameRules.checkWinner(newBoard))
    }

    @Test
    fun aiChoosesCenterOnEmptyBoard() {
        val board = List<Player?>(9) { null }
        assertEquals(4, MinimaxAI.getBestMove(board))
    }
}