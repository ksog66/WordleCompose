package com.example.wordle_compose.ui.play

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.wordle_compose.R
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.data.model.Guess
import com.example.wordle_compose.ui.GameMessage
import com.example.wordle_compose.ui.components.KeyboardComp
import com.example.wordle_compose.ui.components.WordleGrid
import com.example.wordle_compose.ui.main.GameDialog

@Composable
fun PlayRoute(
    keyboardLetters: Map<Char, AlphabetState>,
    guess: List<Guess>,
    gameMessage: GameMessage,
    openDrawer: () -> Unit,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    resetToIdleState: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            WordleTopAppBar(openDrawer)
        }
    ) { innerPadding ->
        WordleBody(
            Modifier.padding(innerPadding),
            keyboardLetters,
            guess,
            gameMessage,
            onLetterClick,
            onEnterClick,
            onBackspaceClick,
            resetToIdleState
        )
    }
}

@Composable
fun WordleBody(
    modifier: Modifier = Modifier,
    keyboardLetters: Map<Char, AlphabetState>,
    guess: List<Guess>,
    gameMessage: GameMessage,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit,
    resetToIdleState: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        WordleGrid(guess = guess)

        Spacer(modifier = Modifier.height(50.dp))

        KeyboardComp(modifier = Modifier,alphabet = keyboardLetters, onLetterClick, onEnterClick, onBackspaceClick)

        GameDialog(modifier = Modifier.padding(4.dp), gameMessage = gameMessage) { resetToIdleState.invoke()}
    }
}

fun generateDummyAnswerList(): List<Guess> {
    val answerMap = listOf(
        Guess(CharArray(5) { ' ' }),
        Guess(CharArray(5) { ' ' }),
        Guess(CharArray(5) { ' ' }),
        Guess(CharArray(5) { ' ' }),
        Guess(CharArray(5) { ' ' })
    )

    return answerMap
}


@Composable
fun WordleTopAppBar(openDrawer: () -> Unit) {

    val title = stringResource(id = R.string.wordle_literal)

    val titleFontColor = if (MaterialTheme.colors.isLight) Color.Black else Color.White

    TopAppBar(
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        ConstraintLayout {
            val (navIconRef, titleRef) = createRefs()

            IconButton(
                modifier = Modifier.constrainAs(navIconRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }, onClick = { openDrawer() }) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = title,
                    tint = titleFontColor
                )
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(titleRef) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                text = title,
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,
                color = titleFontColor,
                textAlign = TextAlign.Center
            )

        }
    }
}

@Preview
@Composable
fun WordleHomePreview() {
    PlayRoute(generateDummyKeyboardLetters(), generateDummyAnswerList(),GameMessage.IdleState, {}, {}, {}, {},{})
}

private fun generateDummyKeyboardLetters(): Map<Char, AlphabetState> {
    val alphabetMap = mutableMapOf<Char, AlphabetState>()

    for (i in 'A'..'Z') {
        alphabetMap.put(i, AlphabetState.values().random())
    }

    return alphabetMap
}

@Preview
@Composable
fun WordleTopAppBarPreview() {
    WordleTopAppBar {

    }
}