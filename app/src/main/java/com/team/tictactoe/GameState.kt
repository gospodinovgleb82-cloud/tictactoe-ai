package com.team.tictactoe

data class GameState(
    val board: List<Player?> = List(9) { null },
)