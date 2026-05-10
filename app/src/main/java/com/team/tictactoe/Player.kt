package com.team.tictactoe

enum class Player {
    HUMAN,
    AI;

    fun symbol(): String = if (this == HUMAN) "X" else "O"
}