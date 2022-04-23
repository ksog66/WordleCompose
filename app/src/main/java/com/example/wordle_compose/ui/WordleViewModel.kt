package com.example.wordle_compose.ui

import androidx.lifecycle.*
import com.example.wordle_compose.data.AlphabetState
import com.example.wordle_compose.data.GameState
import com.example.wordle_compose.data.WordStatus
import com.example.wordle_compose.data.model.Guess
import com.example.wordle_compose.data.repository.WordRepository
import com.example.wordle_compose.utils.toGridArray
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class WordleViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    companion object {
        private const val INIT_WORD_SIZE = 0
        private const val FINAL_WORD_SIZE = 5
        private const val MAX_GUESS_COUNT = 6
    }

    private val gameState = MutableLiveData<GameState>()

    private val _gameEvent = MutableLiveData<GameEvent>()
    val gameEvent: LiveData<GameEvent> = _gameEvent

    private val _gameMessageEvent = MutableLiveData<GameMessage>()
    val gameMessageEvent: LiveData<GameMessage> = _gameMessageEvent

    val keyboardMap = Transformations.map(gameState) {
        it.keyboardState
    }

    val guessList = Transformations.map(gameState) {
        it.guess
    }

    init {
        initGameState()
    }

    fun initGameState() {
        val originalWord = wordRepository.getRandomWord()
        val initialKeyWord = generateInitialKeyBoard()
        val initGuessList = generateInitialGuessList()
        gameState.value = GameState(
            originalAnswer = originalWord,
            keyboardState = initialKeyWord,
            guess = initGuessList,
        )
        _gameEvent.value = GameEvent.InitialState
    }

    fun clearGuessChar() {
        val currentWord = gameState.value?.currentWord ?: return

        if (currentWord.length == INIT_WORD_SIZE) return

        val newWord = currentWord.dropLast(1)
        updateGameState(newWord)
    }

    fun enterChar(letter: Char) {
        val currentState = gameState.value

        if (currentState?.currentWord?.length == FINAL_WORD_SIZE) return

        val newWord = (currentState?.currentWord ?: "") + letter.toString()

        updateGameState(newWord)
    }

    fun submitAnswer() {

        val currentWord = gameState.value?.currentWord ?: return

        viewModelScope.launch {
            if (currentWord.length < FINAL_WORD_SIZE) {
                _gameMessageEvent.value = GameMessage.WordTooShort
            } else {
                when (val wordStatus = getWordStatus(currentWord)) {
                    is WordStatus.NotValid -> {
                        _gameMessageEvent.value = GameMessage.InvalidWord
                    }
                    is WordStatus.Correct -> {
                        val currentGameState = gameState.value ?: return@launch

                        val newGuess = Guess(
                            word = currentWord.toCharArray(),
                            letterStats = List(currentWord.length) { AlphabetState.CORRECT_POSITION }
                        )

                        val guessList = currentGameState.guess.toMutableList()
                        guessList.set(currentGameState.guessNumber, newGuess)

                        gameState.value =
                            currentGameState.copy(
                                guessNumber = currentGameState.guessNumber + 1,
                                guess = guessList.toList(),
                                currentWord = null,
                                keyboardState = updateKeyboardState(newGuess)
                            )

                        _gameEvent.value = GameEvent.GameWon(
                            currentGameState.guessNumber,
                            getGuessGridAsText(
                                currentGameState.guessNumber + 1,
                                guessList.toList()
                            )
                        )
                        return@launch
                    }
                    is WordStatus.Incorrect -> {
                        val currentGameState = gameState.value ?: return@launch

                        val newGuess = Guess(
                            word = currentWord.toCharArray(),
                            letterStats = wordStatus.letterStatus
                        )

                        val guessList = currentGameState.guess.toMutableList()
                        guessList.set(currentGameState.guessNumber, newGuess)

                        gameState.value =
                            currentGameState.copy(
                                guessNumber = currentGameState.guessNumber + 1,
                                guess = guessList.toList(),
                                currentWord = null,
                                keyboardState = updateKeyboardState(newGuess)
                            )
                    }
                }
            }
            if (gameState.value?.guessNumber == MAX_GUESS_COUNT) {
                _gameEvent.value = GameEvent.GameLost(gameState.value!!.originalAnswer)
            }
        }
    }

    private fun updateGameState(newWord: String) {
        val currentState = gameState.value
        val newGuess = Guess(word = newWord.toGridArray())
        val newList = currentState?.guess?.toMutableList()
        newList?.set(currentState.guessNumber, newGuess)
        gameState.value = gameState.value?.copy(
            currentWord = newWord,
            guess = newList?.toList() ?: listOf()
        )
    }

    private fun updateKeyboardState(guess: Guess): Map<Char, AlphabetState> {
        val keyboardState = gameState.value?.keyboardState?.toMutableMap() ?: return mapOf()

        guess.word.forEachIndexed { index, letter ->
            when (val alphabetState = guess.letterStats[index]) {
                AlphabetState.CORRECT_POSITION -> {
                    keyboardState[letter] = alphabetState
                }
                AlphabetState.WRONG_POSITION -> {
                    if (keyboardState[letter] != AlphabetState.CORRECT_POSITION)
                        keyboardState[letter] = alphabetState
                }
                AlphabetState.NOT_PRESENT, AlphabetState.NONE -> {
                    if (keyboardState[letter] != AlphabetState.WRONG_POSITION && keyboardState[letter] != AlphabetState.CORRECT_POSITION) keyboardState[letter] =
                        alphabetState
                }

            }
        }

        return keyboardState
    }

    private fun getWordStatus(guessedWord: String): WordStatus {
        return when {
            !wordRepository.findWord(guessedWord) -> WordStatus.NotValid
            guessedWord == gameState.value?.originalAnswer?.uppercase() -> WordStatus.Correct
            else -> WordStatus.Incorrect(letterStatus = getGuessForCurrentWord(guessedWord))
        }
    }

    private fun getGuessForCurrentWord(guessedWord: String): List<AlphabetState> {
        guessedWord.uppercase()
        val originalWord = gameState.value?.originalAnswer ?: return listOf()

        val missedCharacters = originalWord.mapIndexed { index, char ->
            if (guessedWord[index] != char) char else null
        }.filterNotNull().toMutableList()

        return guessedWord.mapIndexed { guessIndex, guessChar ->
            val ogChar = originalWord[guessIndex]
            val ogIndex = originalWord.indexOf(guessChar)

            when {
                guessChar == ogChar -> AlphabetState.CORRECT_POSITION
                ogIndex >= 0 && missedCharacters.remove(guessChar) -> AlphabetState.WRONG_POSITION
                else -> AlphabetState.NOT_PRESENT
            }
        }
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
            Guess(CharArray(5) { ' ' }),
            Guess(CharArray(5) { ' ' })
        )
    }

    fun setGameEventIdle() {
        _gameMessageEvent.value = GameMessage.IdleState
    }

    private fun getGuessGridAsText(guessNumber: Int, guessGrid: List<Guess>): String {
        var shareText = "Wordle $guessNumber/6 \n"

        guessGrid.forEach { guess ->
            guess.letterStats.forEach { alphabetState ->
                shareText += when (alphabetState) {
                    AlphabetState.NONE -> {
                        ""
                    }
                    AlphabetState.NOT_PRESENT -> {
                        "⬛"
                    }
                    AlphabetState.WRONG_POSITION -> {
                        "\uD83D\uDFE8"
                    }
                    AlphabetState.CORRECT_POSITION -> {
                        "\uD83D\uDFE9"
                    }
                }
            }
            shareText += "\n"
        }

        return shareText
    }
}

class WordleViewModelFactory(private val wordRepository: WordRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WordleViewModel(wordRepository) as T
    }
}

sealed class GameEvent {
    data class GameWon(val guessNumber: Int, val shareText: String) : GameEvent()
    data class GameLost(val originalWord: String) : GameEvent()
    object InitialState : GameEvent()
}

sealed class GameMessage {
    object InvalidWord : GameMessage()
    object WordTooShort : GameMessage()
    object IdleState : GameMessage()
}
