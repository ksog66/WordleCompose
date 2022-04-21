package com.example.wordle_compose.ui.main

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wordle_compose.R
import com.example.wordle_compose.ui.GameMessage
import com.example.wordle_compose.ui.theme.colorTone1
import com.example.wordle_compose.ui.theme.colorTone7
import kotlinx.coroutines.delay

@Composable
fun GameDialog(modifier: Modifier,gameMessage: GameMessage,onDismiss: () ->Unit) {
    when (gameMessage) {
        GameMessage.IdleState -> {
            //Todo something later with this event
        }
        GameMessage.WordTooShort -> {
            AutoDismissToast(modifier = modifier,label = stringResource(id = R.string.word_too_short), onDismiss = onDismiss)
        }
        GameMessage.InvalidWord -> {
            AutoDismissToast(modifier = modifier,label = stringResource(id = R.string.invalid_word), onDismiss = onDismiss)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AutoDismissToast(label: String, modifier: Modifier = Modifier,onDismiss: () -> Unit) {
    var visible by remember { mutableStateOf(true) }
    LaunchedEffect(label) {
        delay(1000)
        visible = false
        delay(300)
        onDismiss.invoke()
    }

    AnimatedVisibility(
        visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Toast(label, modifier)
    }
}

@Composable
fun Toast(label: String, modifier: Modifier = Modifier) {
    Text(
        label,
        modifier
            .clip(MaterialTheme.shapes.small)
            .background(colorTone1)
            .padding(8.dp),
        color = colorTone7,
        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
    )
}