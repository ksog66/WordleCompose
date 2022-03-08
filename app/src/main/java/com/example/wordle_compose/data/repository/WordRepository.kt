package com.example.wordle_compose.data.repository

import android.content.res.AssetManager

class WordRepository(assetManager: AssetManager) {

    private val allWords = assetManager.open("words.txt").readBytes().decodeToString().split("\r\n","\n")
        .filter { it.length == 5 }.map { it.uppercase().trim() }.toSet()


    fun findWord(word:String): Boolean {
        return allWords.contains(word)
    }

    fun getRandomWord(): String = allWords.random()
}