package com.example.wordle_compose.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Alphabet(letter: Char) {
    Box(
        Modifier
            .size(width = 36.dp, height = 48.dp)
            .padding(2.dp)
            .clip(MaterialTheme.shapes.small)
            .padding(horizontal = 2.dp, vertical = 4.dp),
        Alignment.Center
    ) {
        Text(
            letter.toString(),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun AlphabetPreview() {
    Alphabet(letter = 'A')
}