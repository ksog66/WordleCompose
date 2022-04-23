package com.example.wordle_compose.ui.play

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.wordle_compose.R
import com.example.wordle_compose.ui.GameEvent
import com.example.wordle_compose.ui.theme.keyCorrectPlaceBg

private val victoryMessages = arrayOf(
    "Genius",
    "Magnificent",
    "Impressive",
    "Splendid",
    "Great",
    "Nice"
)

@Composable
fun GameEventScreen(gameEvent: GameEvent, resetGame: () -> Unit) {
    when (gameEvent) {
        is GameEvent.GameLost -> {
            GameLostDialog(gameEvent.originalWord, resetGame)
        }
        is GameEvent.GameWon -> {
            GameWonDialog(gameEvent.guessNumber, resetGame)
        }
        GameEvent.InitialState -> {}
    }
}

@Composable
fun GameLostDialog(originalWord: String, resetGame: () -> Unit) {
    AlertDialog(
        backgroundColor = Color.Black,
        shape = RoundedCornerShape(8.dp),
        text = null,
        title = null,
        onDismissRequest = { resetGame.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        buttons = {
            Column(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 40.dp,
                    start = 20.dp,
                    end = 20.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { resetGame.invoke() },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_desc),
                    colorFilter = ColorFilter.tint(Color.White)
                )


                Text(
                    text = stringResource(id = R.string.oops_better_luck_next_time),
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                Text(text = buildAnnotatedString {
                    append("${stringResource(id = R.string.original_word_was)} ")

                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = keyCorrectPlaceBg
                        )
                    ) {
                        append(originalWord)
                    }
                }, color = Color.White, fontSize = 15.sp)

                Spacer(Modifier.height(10.dp))

                Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                    PlayAgainButton(resetGame)
                    Divider(
                        Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxHeight()
                            .width(2.dp), color = Color.White)
                    StatsButton()
                }
            }
        }
    )
}

@Composable
fun GameWonDialog(guessNumber: Int, resetGame: () -> Unit) {
    AlertDialog(
        backgroundColor = Color.Black,
        shape = RoundedCornerShape(8.dp),
        text = null,
        title = null,
        onDismissRequest = { resetGame.invoke() },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        buttons = {
            Column(
                modifier = Modifier.padding(
                    top = 10.dp,
                    bottom = 40.dp,
                    start = 20.dp,
                    end = 20.dp
                ), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { resetGame.invoke() },
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.close_desc),
                    colorFilter = ColorFilter.tint(Color.White)
                )


                Text(
                    text = victoryMessages[guessNumber],
                    color = Color.White,
                    fontSize = 20.sp
                )

                Spacer(Modifier.height(10.dp))

                val modifier = Modifier
                Row(modifier = modifier.height(IntrinsicSize.Min)) {
                    PlayAgainButton(resetGame)
                    Divider(
                        Modifier
                            .padding(horizontal = 5.dp)
                            .fillMaxHeight()
                            .width(2.dp), color = Color.White)
                    StatsButton()
                }

                Spacer(modifier = Modifier.height(10.dp))

                ShareButton(modifier.align(Alignment.CenterHorizontally))
            }
        }
    )
}

@Composable
fun PlayAgainButton(playAgain: () -> Unit) {
    Button(onClick = { playAgain.invoke() }, colors = ButtonDefaults.buttonColors(backgroundColor = keyCorrectPlaceBg)) {
        Text(
            text = stringResource(id = R.string.play_again),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = Color.White
        )
    }
}

@Composable
fun StatsButton() {
    Button(onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = keyCorrectPlaceBg)) {
        Text(
            text = stringResource(id = R.string.stats_title),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = Color.White
        )
        Image(
            imageVector = Icons.Filled.BarChart,
            contentDescription = stringResource(id = R.string.stats_title),
            contentScale = ContentScale.Inside
        )
    }
}

@Composable
fun ShareButton(modifier: Modifier,shareText:String) {
    Button(modifier = modifier,onClick = {}, colors = ButtonDefaults.buttonColors(backgroundColor = keyCorrectPlaceBg)) {
        Text(
            text = stringResource(id = R.string.share),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 15.sp,
            color = Color.White
        )
        Image(
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.share),
            contentScale = ContentScale.Inside
        )
    }
}

@Preview
@Composable
fun GameLostDialogPreview() {
    GameLostDialog(originalWord = "CRANE", {})
}

@Preview
@Composable
fun GameWonDialogPreview() {
    GameWonDialog(4, {})
}