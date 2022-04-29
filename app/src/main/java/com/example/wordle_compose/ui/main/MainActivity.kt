package com.example.wordle_compose.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wordle_compose.WordleApplication
import com.example.wordle_compose.ui.theme.WordleComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appContainer = (application as WordleApplication).appContainer
        setContent {
            WordleComposeTheme {
                WordleHomeScreen(appContainer)
            }
        }
    }
}