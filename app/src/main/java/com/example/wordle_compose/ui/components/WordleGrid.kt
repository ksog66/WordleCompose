package com.example.wordle_compose.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.ui.play.generateDummyAnswerList
import com.example.wordle_compose.ui.theme.*

@Composable
fun WordleGrid(answers:Map<String,List<AlphabetState>>) {
    Column {
        answers.keys.forEach { answer ->
            Row {
                answer.forEachIndexed { index, char ->
                    val letterState = answers[answer]?.get(index) ?: AlphabetState.NONE
                    WordleBox(letter = char, alphabetState = letterState)
                }
            }
        }
    }
}

@Composable
fun WordleBox(letter: Char, alphabetState: AlphabetState) {

    val backgroundColor = animateColorAsState(
        targetValue = when (alphabetState) {
            AlphabetState.NONE -> Color.Transparent
            AlphabetState.NOT_PRESENT -> keyMissingBg
            AlphabetState.WRONG_POSITION -> keyWrongPlaceBg
            AlphabetState.CORRECT_POSITION -> keyCorrectPlaceBg
        }
    )

    val fontTextColor = animateColorAsState(
        targetValue = when (alphabetState) {
            AlphabetState.NONE -> if (MaterialTheme.colors.isLight) keyFontColorDark else keyFontColorLight
            else -> keyFontColorLight
        }
    )

    val borderColor = animateColorAsState(
        targetValue =
        when (alphabetState) {
            AlphabetState.NONE -> if (MaterialTheme.colors.isLight) keyFontColorDark else keyFontColorLight
            else -> Color.Transparent
        }
    )

    Box(
        Modifier
            .size(50.dp)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(BorderStroke(1.dp, borderColor.value))
            .background(color = backgroundColor.value),
        Alignment.Center,
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = fontTextColor.value
        )
    }
}

@Preview
@Composable
fun WordleGridPreview() {
    WordleGrid(answers = generateDummyAnswerList())
}
@Preview
@Composable
fun WordleBoxPreview() {
    WordleBox('A', AlphabetState.CORRECT_POSITION)
}