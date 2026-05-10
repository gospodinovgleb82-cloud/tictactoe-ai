package com.team.tictactoe

data class GameState(
    val board: List<Player?> = List(9) { null },
    val currentPlayer: Player = Player.X,
    val winner: Player? = null,
    val isGameOver: Boolean = false
)