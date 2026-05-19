// Difficulty.kt
package com.team.tictactoe

enum class Difficulty {
    EASY,   // Лёгкий  — случайные ходы
    MEDIUM, // Средний — блокирует, иногда ошибается
    HARD;   // Сложный — Minimax, никогда не проигрывает

    fun label(): String = when (this) {
        EASY   -> "Лёгкий"
        MEDIUM -> "Средний"
        HARD   -> "Сложный"
    }
}