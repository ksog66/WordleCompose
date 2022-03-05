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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.wordle_compose.R
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.ui.components.KeyboardComp
import com.example.wordle_compose.ui.components.WordleGrid

@Composable
fun PlayRoute(
    keyboardLetters:Map<Char,AlphabetState>,
    openDrawer: () -> Unit,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            WordleTopAppBar(openDrawer)
        }
    ) { innerPadding ->
        WordleBody(Modifier.padding(innerPadding),keyboardLetters, onLetterClick,onEnterClick, onBackspaceClick)
    }
}

@Composable
fun WordleBody(
    modifier: Modifier = Modifier,
    keyboardLetters:Map<Char,AlphabetState>,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))

        WordleGrid(answers = generateDummyAnswerList())

        Spacer(modifier = Modifier.height(20.dp))

        KeyboardComp(alphabet = keyboardLetters, onLetterClick, onEnterClick, onBackspaceClick)
    }
}

fun generateDummyAnswerList(): Map<String,List<AlphabetState>> {
    val answerMap = mutableMapOf<String,List<AlphabetState>>(
        "ASDFG" to List(5) {AlphabetState.NONE},
        "CLOWN" to List(5) {AlphabetState.NOT_PRESENT},
        "MOURN" to List(5) {AlphabetState.CORRECT_POSITION},
        "CLICK" to List(5) {AlphabetState.NONE},
        "GREAT" to List(5) {AlphabetState.WRONG_POSITION},
        "SNAKE" to List(5) {AlphabetState.NOT_PRESENT}
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
    PlayRoute(generateDummyKeyboardLetters(),{},{},{},{})
}

private fun generateDummyKeyboardLetters() :Map<Char,AlphabetState> {
    val alphabetMap = mutableMapOf<Char,AlphabetState>()

    for (i in 'A'..'Z') {
        alphabetMap.put(i,AlphabetState.values().random())
    }

    return alphabetMap
}
@Preview
@Composable
fun WordleTopAppBarPreview() {
    WordleTopAppBar {

    }
}