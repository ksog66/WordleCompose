package com.example.wordle_compose.ui.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle_compose.R
import com.example.wordle_compose.data.model.GameStats
import com.example.wordle_compose.ui.theme.keyCorrectPlaceBg
import com.example.wordle_compose.ui.theme.keyIdleBackgroundDark

@Composable
fun StatisticsRoute(gameStats: GameStats) {
    val modifier = Modifier
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(top = 5.dp, start = 5.dp, end = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.stats_title).uppercase(),
            color = Color.Black,
            fontSize = 35.sp
        )

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        GameStatsRow(gameStats)

        Spacer(
            Modifier
                .fillMaxWidth()
                .height(20.dp)
        )

        Text(
            text = stringResource(id = R.string.guess_distribution).uppercase(),
            color = Color.Black,
            fontSize = 25.sp
        )
        
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(5.dp))

        GuessDistribution(gameStats.guessFrequency)
    }
}

@Composable
fun GameStatsRow(gameStats: GameStats) {
    val gameWinPercentage =
        if (gameStats.gamePlayed == 0) 0 else (gameStats.gameWon / gameStats.gamePlayed) * 100
    Row(
        modifier = Modifier
            .padding(vertical = 5.dp)
            .fillMaxWidth()
            .clickable { },
        horizontalArrangement = Arrangement.SpaceEvenly
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
    Column(Modifier.width(IntrinsicSize.Max), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            modifier = Modifier.width(IntrinsicSize.Min),
            textAlign = TextAlign.Center,
            text = value.toString(),
            fontSize = 30.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.width(IntrinsicSize.Min),
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Clip,
            text = label,
            fontSize = 10.sp,
            color = Color.Black,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GuessDistribution(guessFrequency: List<Int>) {
    Column {
        guessFrequency.forEachIndexed { index, value ->
            GuessHorizontalBar(index+1,value+1)
        }
    }
}

@Composable
fun GuessHorizontalBar(index:Int,barSize:Int) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.width(IntrinsicSize.Min),
            text = index.toString(),
            color = Color.Black,
            fontSize = 10.sp
        )
        
        Canvas(modifier = Modifier.width((barSize*2).dp).height(20.dp).background(Color.White)) {
            drawRect(color = keyIdleBackgroundDark, topLeft = Offset(0f,1f),size = size)
        }
        
    }
}