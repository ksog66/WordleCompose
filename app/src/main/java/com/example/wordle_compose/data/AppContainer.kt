package com.example.wordle_compose.data

import android.content.Context
import com.example.wordle_compose.data.repository.WordRepository

interface AppContainer {
    val wordRepository: WordRepository
    val preferencesManager: PreferencesManager
}

class AppContainerImpl(
    private val applicationContext: Context
) : AppContainer {
    override val wordRepository: WordRepository by lazy {
        WordRepository(applicationContext.assets)
    }

    override val preferencesManager: PreferencesManager
        get() = PreferencesManager(applicationContext)
}