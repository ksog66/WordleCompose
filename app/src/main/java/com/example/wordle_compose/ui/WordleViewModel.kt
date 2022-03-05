package com.example.wordle_compose.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wordle_compose.data.AlphabetState
import kotlinx.coroutines.flow.flow

class WordleViewModel: ViewModel() {

    private var _keyboardMap = MutableLiveData<Map<Char,AlphabetState>>()
    val keyboardMap: LiveData<Map<Char,AlphabetState>> = _keyboardMap

    private var currentGuess: CharArray = CharArray(5)

    init {
        _keyboardMap.value = generateInitialKeyBoard()
    }

    fun generateInitialKeyBoard(): Map<Char,AlphabetState> {
        val alphabetMap = mutableMapOf<Char,AlphabetState>()

        for (i in 'A'..'Z') {
            alphabetMap.put(i,AlphabetState.values().random())
        }

        return alphabetMap
    }
}




class WordleViewModelFactory: ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordleViewModel() as T
    }
}