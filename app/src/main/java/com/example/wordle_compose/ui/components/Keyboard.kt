package com.example.wordle_compose.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.ui.theme.*


private const val KEY_ROW_1 = "QWERTYUIOP"
private const val KEY_ROW_2 = "ASDFGHJKL"
private const val KEY_ROW_3 = "_ZXCVBNM<"

@Composable
fun KeyboardComp(
    alphabet: Map<Char, AlphabetState>,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        listOf(KEY_ROW_1, KEY_ROW_2, KEY_ROW_3).forEach { keyRow ->
            Row(
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                keyRow.forEach {
                    Alphabet(
                        letter = it,
                        alphabetState = alphabet[it] ?: AlphabetState.NONE,
                        onLetterClick = {},
                        onBackspaceClick = {},
                        onEnterClick = {})
                }
            }
        }
    }
}

@Composable
fun Alphabet(
    letter: Char,
    alphabetState: AlphabetState,
    onLetterClick: (Char) -> Unit,
    onEnterClick: () -> Unit,
    onBackspaceClick: () -> Unit
) {
    val backgroundColor = animateColorAsState(targetValue = when(alphabetState) {
        AlphabetState.NONE -> if (MaterialTheme.colors.isLight) keyIdleBackgroundLight else keyIdleBackgroundDark
        AlphabetState.NOT_PRESENT -> keyMissingBg
        AlphabetState.WRONG_POSITION -> keyWrongPlaceBg
        AlphabetState.CORRECT_POSITION -> keyCorrectPlaceBg
    })

    val fontTextColor = animateColorAsState(
        targetValue = when (alphabetState) {
            AlphabetState.NONE -> if (MaterialTheme.colors.isLight) keyFontColorDark else keyFontColorLight
            else -> keyFontColorLight
        }
    )
    Box(
        Modifier
            .height(48.dp)
            .width(IntrinsicSize.Max)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.small)
            .border(BorderStroke(1.dp, backgroundColor.value))
            .background(color = backgroundColor.value)
            .clickable {
                if (letter == '_') {
                    onEnterClick()
                } else if (letter == '<') {
                    onBackspaceClick()
                } else {
                    onLetterClick(letter)
                }
            },
        Alignment.Center,

        ) {
        if (letter == '_') {
            Text(
                text = "Enter",
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(IntrinsicSize.Max).padding(horizontal = 4.dp, vertical = 4.dp),
                textAlign = TextAlign.Center,
                color = fontTextColor.value
            )
        } else if (letter == '<') {
            Icon(
                imageVector = Icons.Outlined.Backspace,
                contentDescription = null,
                tint = if (MaterialTheme.colors.isLight) Color.Black else Color.White,
                modifier = Modifier.size(24.dp).padding(horizontal = 4.dp)
            )
        } else {
            Text(
                letter.toString(),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp),
                textAlign = TextAlign.Center,
                color = fontTextColor.value
            )
        }
    }
}

@Preview
@Composable
fun KeyboardPreview() {
    KeyboardComp(mapOf(),{},{},{})
}

@Preview
@Composable
fun AlphabetPreview() {
    Alphabet(letter = 'A', AlphabetState.CORRECT_POSITION, onBackspaceClick = {}, onLetterClick = {}, onEnterClick = {})
}