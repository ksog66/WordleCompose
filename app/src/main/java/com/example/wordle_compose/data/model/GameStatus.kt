package com.example.wordle_compose.data

import com.example.wordle_compose.data.model.Guess
import kotlin.properties.Delegates

enum class AlphabetState {
    NONE,
    NOT_PRESENT,
    WRONG_POSITION,
    CORRECT_POSITION;
}

data class GameState(
    val originalAnswer:String,
    val keyboardState: Map<Char,AlphabetState>,
    val guess: List<Guess>,
    val guessNumber: Int = 1,
    val currentWord:String? = null,
)

sealed class WordStatus {
    object NotValid : WordStatus()
    object Correct : WordStatus()

    data class Incorrect(
        val letterStatus: List<AlphabetState>
    ) : WordStatus() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Incorrect

            if (letterStatus != other.letterStatus) return false

            return true
        }

        override fun hashCode(): Int {
            return letterStatus.hashCode()
        }
    }
}