package com.example.wordle_compose.ui.statistics

import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateRectAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle_compose.R
import com.example.wordle_compose.data.model.GameStats
import com.example.wordle_compose.ui.theme.keyIdleBackgroundDark

private const val TAG = "StatisticsRoute"

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
            .height(20.dp))

        GuessDistribution(gameStats.guessFrequency)
    }
}

@Composable
fun GameStatsRow(gameStats: GameStats) {
    val gameWinPercentage =
        if (gameStats.gamePlayed == 0) 0 else (gameStats.gameWon.toFloat() / gameStats.gamePlayed.toFloat()) * 100
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
            value = gameWinPercentage.toInt(),
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
    Log.d(TAG, "GuessDistribution: $guessFrequency")
    Column() {
        guessFrequency.forEachIndexed { index, value ->
            GuessHorizontalBar(index+1,value+1)
            Spacer(
                Modifier
                    .fillMaxWidth()
                    .height(5.dp))
        }
    }
}

@Composable
fun GuessHorizontalBar(index:Int,barSize:Int) {
    Log.d(TAG, "GuessHorizontalBar: $barSize")
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            modifier = Modifier.width(IntrinsicSize.Min),
            text = index.toString(),
            color = Color.Black,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier
            .height(1.dp)
            .width(2.dp))
        val barWidth = barSize * 20

        val paint = Paint().apply {
            textAlign = Paint.Align.CENTER
            textSize = 40f
            color = Color.White.toArgb()
        }

        val xPos = if(barSize == 1) barWidth/4 else barWidth - 10

        Canvas(modifier = Modifier
            .width(animateDpAsState(targetValue = barWidth.dp).value)
            .height(30.dp)
            .background(Color.White)) {
            drawRect(color = keyIdleBackgroundDark, topLeft = Offset(0f,1f),size = size)
            drawContext.canvas.nativeCanvas.drawText("${barSize-1}",center.x+xPos,center.y+15,paint)
        }
    }
}