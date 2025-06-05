package site.smartenglish.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.WordBook
import site.smartenglish.repository.LearnedRepository
import site.smartenglish.repository.UserRepository
import site.smartenglish.repository.UsingRepository
import javax.inject.Inject

@HiltViewModel
class DashBoardViewmodel @Inject constructor(
    private val userRepository: UserRepository,
    private val learnedRepository: LearnedRepository,
    private val usingRepository: UsingRepository
):ViewModel() {
    // 正在学习
    private val _wordbookUrl = MutableStateFlow("")
    private val _wordbookLearned = MutableStateFlow(0)
    private val _wordbookTotal = MutableStateFlow(0)
    val wordbookUrl = _wordbookUrl.asStateFlow()
    val wordbookLearned = _wordbookLearned.asStateFlow()
    val wordbookTotal = _wordbookTotal.asStateFlow()


    // 我的数据
    private val _todayWord = MutableStateFlow(0)
    private val _totalWord = MutableStateFlow(0)
    private val _todayTime = MutableStateFlow(0)
    private val _totalTime = MutableStateFlow(0)
    val todayWord = _todayWord.asStateFlow()
    val totalWord = _totalWord.asStateFlow()
    val todayTime = _todayTime.asStateFlow()
    val totalTime = _totalTime.asStateFlow()

    private val _currentWordBookId = MutableStateFlow(0)
    val currentWordBookId = _currentWordBookId.asStateFlow()


    fun getDashBoardData() {
        viewModelScope.launch {
            try {
                val profile = userRepository.getProfile()
                val learnedListSize = learnedRepository.getLearnedWordList()?.size?:0
                val usingTime = usingRepository.getUsingTime("learn")
                val wordbook = profile.wordbook?: WordBook()

                _wordbookUrl.value = wordbook.cover?:""
                _wordbookTotal.value = wordbook.wordcount?:0
                _wordbookLearned.value = _wordbookTotal.value - (profile.new_word_count?:0)

                _totalWord.value = learnedListSize
                _totalTime.value = usingTime?:0

                _currentWordBookId.value = profile.wordbook?.id?:0
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }




}