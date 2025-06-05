package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.WordBook
import site.smartenglish.repository.UserRepository
import site.smartenglish.repository.WordBookRepository
import javax.inject.Inject

@HiltViewModel
class WordBookViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val wordBookRepository: WordBookRepository
) : ViewModel() {

    private val _wordbooks = MutableStateFlow<List<WordBook>>(emptyList())
    val wordbooks = _wordbooks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // 单词列表状态
    private val _wordList = MutableStateFlow<List<String>>(emptyList())
    val wordList = _wordList.asStateFlow()

    // 当前词书ID
    private val _currentWordBookId = MutableStateFlow(0)
    val currentWordBookId = _currentWordBookId.asStateFlow()

    // 当前词书名称
    private val _currentWordBookName = MutableStateFlow("")
    val currentWordBookName = _currentWordBookName.asStateFlow()


    init {
        getWordBooks()
    }

    fun getWordBooks() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val books = fetchWordBooks()
                _wordbooks.value = books
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectWordBook(wordbookId: Int): Boolean {
        var success = false
        viewModelScope.launch {
            try {
                userRepository.changeProfile(wordbookId = wordbookId)
                success = true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return success
    }

    // 获取词书列表
   private suspend fun fetchWordBooks(): List<WordBook> {
       try {
           return wordBookRepository.getWordBooks()?.filterNotNull()
               ?.filter {
                   it.id != null && it.name != null && it.wordcount != null
               }?.map {
                   WordBook(
                       id = it.id!!,
                       name = it.name!!,
                       cover = it.cover,
                       wordcount = it.wordcount!!
                   )
               } ?: emptyList()
       } catch (e: Exception) {
           e.printStackTrace()
           return emptyList()  // 在异常情况下返回空列表
       }
   }

    fun getWordBookDetail(wordBookId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val profile = userRepository.getProfile()
                val response = wordBookRepository.getWordBookDetail(wordBookId)
                _currentWordBookId.value = profile.wordbook?.id?:0
                _currentWordBookName.value = profile.wordbook?.name?:""

                // 处理单词列表
                val words = response?.mapNotNull {
                    it
                } ?: emptyList()
                _wordList.value = words
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}