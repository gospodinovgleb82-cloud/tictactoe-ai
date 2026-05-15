package com.team.tictactoe
data class GameState(
    val board: List<Player?> = List(9) { null },
    val currentTurn: Player = Player.HUMAN,
    val status: GameStatus = GameStatus.PLAYING,
    val boardSize: Int = 3
)

enum class GameStatus {
    PLAYING,
    HUMAN_WIN,
    AI_WIN,
    DRAW
}