package site.smartenglish.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.Updater
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetNWordBookListResponseElement
import site.smartenglish.repository.LearnedRepository
import site.smartenglish.repository.NWordBookRepository
import site.smartenglish.repository.WordBookRepository
import site.smartenglish.repository.WordRepository
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class DictationViewModel @Inject constructor(
    private val wordBookRepository: WordBookRepository,
    private val nWordBookRepository: NWordBookRepository,
    private val wordRepository: WordRepository,
    private val learnedRepository: LearnedRepository
) : ViewModel() {
    private val _currentWords = MutableStateFlow(listOf<String>())
    val currentWords = _currentWords.asStateFlow()

    private val _currentWordIndex = MutableStateFlow(0)
    val currentWordIndex = _currentWordIndex.asStateFlow()

    private val _currentSymbol = MutableStateFlow("")
    val currentSymbol = _currentSymbol.asStateFlow()

    private val _isCurrentFavourite = MutableStateFlow(false)
    val isCurrentFavourite = _isCurrentFavourite.asStateFlow()

    private val _currentMeanings = MutableStateFlow(listOf<String>())
    val currentMeanings = _currentMeanings.asStateFlow()

    private val _currentWordTypes = MutableStateFlow(listOf<String>())
    val currentWordType = _currentWordTypes.asStateFlow()

    private val _currentSoundType = MutableStateFlow("")
    val currentSoundType = _currentSoundType.asStateFlow()

    private val _currentSoundUrl = MutableStateFlow("")
    val currentSoundUrl = _currentSoundUrl.asStateFlow()

    private val _currentWordPlayedTime = MutableStateFlow(0)
    val currentWordPlayedTime = _currentWordPlayedTime.asStateFlow()

    private val _learnedWordCount = MutableStateFlow(0)
    val learnedWordCount = _learnedWordCount.asStateFlow()

    private val _NWordBookList = MutableStateFlow(listOf<GetNWordBookListResponseElement>())
    val NWordBookList = _NWordBookList.asStateFlow()

    private val _finished = MutableStateFlow(false)
    val finished = _finished.asStateFlow()

    init {
        fetchLearnedWords()
        viewModelScope.launch {
            _learnedWordCount.value = learnedRepository.getTodayLearnedWordCount()
            try {
                _NWordBookList.value = (nWordBookRepository.getNWordBookList()?.filterNotNull()
                    ?: emptyList())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val currentWord: String
        get() = _currentWords.value.getOrNull(_currentWordIndex.value) ?: ""

    fun moveToLastWord() {
        if (_currentWords.value.isNotEmpty() && _currentWordIndex.value > 0) {
            _currentWordIndex.value --
            _currentWordPlayedTime.value = 0
            viewModelScope.launch {
                UpdateCurrent()
            }
        }
    }

    fun moveToNextWord() {
        if (_currentWords.value.isNotEmpty() ) {
            if(_currentWordIndex.value < _currentWords.value.size - 1){
                _currentWordIndex.value ++
                _currentWordPlayedTime.value = 0
                viewModelScope.launch {
                    UpdateCurrent()
                }
            }else{
                _finished.value = true
            }
        }
    }

    private suspend fun UpdateCurrent() {
        if (_currentWords.value.isNotEmpty()) {
            val wordInfo= wordRepository.getWordInfo(_currentWords.value[_currentWordIndex.value])
            _currentSymbol.value = wordInfo.phonetic?: ""
            _isCurrentFavourite.value =
                _NWordBookList.value[0].id?.let {
                    nWordBookRepository.checkNWordBook(currentWord,
                        it
                    )
                } == true // 假设0是默认的生词本ID
            _currentMeanings.value = wordInfo.explanations ?: emptyList()
            _currentWordTypes.value = emptyList<String>()//TODO 真有这个吗？
            _currentSoundType.value = "日"//TODO 真有这个吗？
            _currentSoundUrl.value = wordInfo.pronunciation.toString()
            Log.e("DictationViewModel",currentSoundUrl.value)
        }
    }

    // 获取20新词
    fun fetchNewWords() {
        viewModelScope.launch {
            try {
                _currentWords.value =  wordBookRepository.get20NewWords()?.filterNotNull()
                    ?.filter {
                        it.isNotEmpty() && it.length > 1
                    }?: emptyList()
                _currentWordIndex.value = 0
                UpdateCurrent()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 获取20已经学习词
    fun fetchLearnedWords() {
        viewModelScope.launch {
            try {
                val list = learnedRepository.getLearnedWordList()?.mapNotNull { x -> x.word }?: emptyList()
                _currentWords.value = list.subList(0, minOf(20,list.count())).shuffled()
                UpdateCurrent( )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 获取20生词本词
    fun fetchNWordBookWords(id:Int){
        viewModelScope.launch {
            try {
                val list = nWordBookRepository.getNWordBookWords(id)?.filterNotNull()?: emptyList()
                _currentWords.value = list.shuffled().subList(0, minOf(20,list.count()))
                UpdateCurrent( )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // 添加到生词本
    fun switchWordInNWordBook(word: String) {
        viewModelScope.launch {
            try {
                _NWordBookList.value[0].id?.let { nWordBookRepository.addNWord(word, it) } // 假设0是默认的生词本ID
                UpdateCurrent()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun wordPlayed() {
        _currentWordPlayedTime.value += 1
    }
}