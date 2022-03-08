package com.example.wordle_compose.ui

import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.*
import com.example.wordle_compose.R
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.data.GameState
import com.example.wordle_compose.data.WordStatus
import com.example.wordle_compose.data.model.Guess
import com.example.wordle_compose.data.repository.WordRepository

class WordleViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    companion object {
        private const val INIT_WORD_SIZE = 0
        private const val FINAL_WORD_SIZE = 5
    }

    private val gameState = MutableLiveData<GameState>()

    val toastEvent = MutableLiveData<ToastEvent>()

    val keyboardMap = Transformations.map(gameState) {
        it.keyboardState
    }

    val guessList = Transformations.map(gameState) {
        it.guess
    }

    init {
        val originalWord = wordRepository.getRandomWord()
        val initialKeyWord = generateInitialKeyBoard()
        val initGuessList = generateInitialGuessList()
        gameState.value = GameState(
            originalAnswer = originalWord,
            keyboardState = initialKeyWord,
            guess = initGuessList,
        )
    }

    fun clearGuessChar() {
        val currentWord = gameState.value?.currentWord ?: return

        if (currentWord.length == INIT_WORD_SIZE) return

        gameState.value = gameState.value?.copy(currentWord = currentWord.dropLast(1))
    }

    fun enterChar(letter: Char) {
        val currentState = gameState.value

        if (currentState?.currentWord?.length == FINAL_WORD_SIZE) return

        val newWord = currentState?.currentWord ?: "" + letter.toString()
        val currentGuess = currentState?.guess?.last()?.copy(word = newWord.toCharArray()) ?: return

        gameState.value = gameState.value?.copy(
            currentWord = newWord,
            guess = currentState.guess.subList(0,currentState.guess.size-1) + currentGuess
        )

    }

    fun submitAnswer() {
        val currentWord = gameState.value?.currentWord ?: return

        if (currentWord.length < FINAL_WORD_SIZE) {
            toastEvent.value = ToastEvent.WORD_SHORT
        } else {
            when (val wordStatus = getWordStatus(currentWord)) {
                is WordStatus.NotValid -> {
                    toastEvent.value = ToastEvent.INVALID_WORD
                }
                is WordStatus.Correct -> {
                    toastEvent.value = ToastEvent.SUCCESS_MESSAGE
                }
                is WordStatus.Incorrect -> {
                    val currentGameState = gameState.value ?: return
                    val newGuess = Guess(
                        word = currentWord.toCharArray(),
                        letterStats = wordStatus.letterStatus
                    )
                    updateKeyboardState(newGuess)
                    gameState.value =
                        currentGameState.copy(guess = currentGameState.guess + newGuess)
                }
            }
        }
    }

    private fun updateKeyboardState(guess: Guess) {
        val keyboardState = gameState.value?.keyboardState?.toMutableMap() ?: return

        guess.word.filter { keyboardState[it] != AlphabetState.CORRECT_POSITION }
            .forEachIndexed { index, letter ->
                when (val alphabetState = guess.letterStats[index]) {
                    AlphabetState.NOT_PRESENT -> {
                        if (keyboardState[letter] != AlphabetState.WRONG_POSITION) keyboardState[letter] =
                            alphabetState
                    }
                    AlphabetState.WRONG_POSITION, AlphabetState.CORRECT_POSITION -> {
                        keyboardState[letter] = alphabetState
                    }
                    else -> {}
                }
            }

        gameState.value = gameState.value?.copy(keyboardState = keyboardState.toMap())
    }

    private fun getWordStatus(guessedWord: String): WordStatus {
        return when {
            !wordRepository.findWord(guessedWord) -> WordStatus.NotValid
            guessedWord == gameState.value?.originalAnswer?.uppercase() -> WordStatus.Correct
            else -> WordStatus.Incorrect(letterStatus = getGuessForCurrentWord(guessedWord))
        }
    }

    private fun getGuessForCurrentWord(guessedWord: String): List<AlphabetState> {
        val lettersState = mutableListOf<AlphabetState>()

        guessedWord.uppercase()
        val originalWord = gameState.value?.originalAnswer ?: return listOf()

        guessedWord.forEachIndexed guessLoop@{ guessIndex, guessChar ->
            var letterState: AlphabetState = AlphabetState.NOT_PRESENT
            originalWord.forEachIndexed originalLoop@{ ogIndex, ogChar ->
                if (guessChar == ogChar) {
                    if (guessIndex == ogIndex) {
                        letterState = AlphabetState.CORRECT_POSITION
                        return@originalLoop
                    }

                    letterState = AlphabetState.WRONG_POSITION
                }
            }
            lettersState[guessIndex] = letterState
        }

        return lettersState
    }

    private fun generateInitialKeyBoard(): Map<Char, AlphabetState> {
        val alphabetMap = mutableMapOf<Char, AlphabetState>()

        for (i in 'A'..'Z') {
            alphabetMap.put(i, AlphabetState.NONE)
        }

        return alphabetMap
    }

    private fun generateInitialGuessList(): List<Guess> {
        return mutableListOf(
            Guess(CharArray(5) { ' ' }),
            Guess(CharArray(5) { ' ' }),
            Guess(CharArray(5) { ' ' }),
            Guess(CharArray(5) { ' ' }),
            Guess(CharArray(5) { ' ' })
        )
    }
}

class WordleViewModelFactory(private val wordRepository: WordRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordleViewModel(wordRepository) as T
    }
}

enum class ToastEvent {
    IDLE,
    WORD_SHORT,
    SUCCESS_MESSAGE,
    INVALID_WORD
}