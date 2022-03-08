package com.example.wordle_compose.data.model

import com.example.wordle_compose.data.AlphabetState

data class Guess(
    var word: CharArray,
    val letterStats: List<AlphabetState> = List(5) { AlphabetState.NONE }
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Guess

        if (!word.contentEquals(other.word)) return false
        if (letterStats != other.letterStats) return false

        return true
    }

    override fun hashCode(): Int {
        var result = word.contentHashCode()
        result = 31 * result + letterStats.hashCode()
        return result
    }
}