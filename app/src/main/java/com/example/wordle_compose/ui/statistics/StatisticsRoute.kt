package com.example.wordle_compose.ui.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle_compose.R
import com.example.wordle_compose.data.model.GameStats

@Composable
fun StatisticsRoute(gameStats: GameStats) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp)
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.stats_title).uppercase(),
            color = Color.White,
            fontSize = 35.sp
        )

        Divider()

        GameStatsRow(gameStats)
    }
}

@Composable
fun GameStatsRow(gameStats: GameStats) {
    val gameWinPercentage = if (gameStats.gamePlayed == 0) 0 else (gameStats.gameWon / gameStats.gamePlayed) * 100
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        StatsComponent(
            value = gameStats.gamePlayed,
            label = stringResource(id = R.string.game_played)
        )
        StatsComponent(
            value = gameWinPercentage,
            label = stringResource(id = R.string.win_percentage)
        )
        StatsComponent(
            value = gameStats.currentStreak,
            label = stringResource(id = R.string.current_streak)
        )
        StatsComponent(
            value = gameStats.maxStreak,
            label = stringResource(id = R.string.max_streak)
        )
    }
}

@Composable
fun StatsComponent(value: Int, label: String) {
    Column(Modifier.width(IntrinsicSize.Max)) {
        Text(
            text = value.toString(),
            fontSize = 30.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color.White,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Light
        )
    }
}
