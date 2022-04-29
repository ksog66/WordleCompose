package com.example.wordle_compose.data

import android.content.Context
import android.util.Log
import androidx.datastore.dataStore
import com.example.wordle_compose.data.model.GameStatSerializer
import com.example.wordle_compose.data.model.GameStats
import kotlinx.coroutines.flow.catch
import java.io.IOException


private const val TAG = "PreferencesManager"

private val Context.dataStore by dataStore("game-stats.json", GameStatSerializer)


class PreferencesManager(context: Context) {

    private val appContext = context.applicationContext

    val gameStatsFlow = appContext.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(GameStats())
            } else {
                throw exception
            }
        }

    suspend fun updateDataStore(gameStats: GameStats) {
        appContext.dataStore.updateData {
            it.copy(
                gamePlayed = gameStats.gamePlayed,
                gameWon = gameStats.gameWon,
                currentStreak = gameStats.currentStreak,
                maxStreak = gameStats.maxStreak,
                guessFrequency = gameStats.guessFrequency
            )
        }
    }
}