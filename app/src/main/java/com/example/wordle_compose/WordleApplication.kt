package com.example.wordle_compose

import android.app.Application
import com.example.wordle_compose.data.AppContainer
import com.example.wordle_compose.data.AppContainerImpl

class WordleApplication :Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl(this)
    }
}