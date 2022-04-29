package com.example.wordle_compose.data.model

import androidx.datastore.preferences.protobuf.Internal
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable

@Serializable
data class GameStats(
    val gamePlayed: Int = 0,
    val gameWon: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val guessFrequency: List<Int> = List(6) {0}
)