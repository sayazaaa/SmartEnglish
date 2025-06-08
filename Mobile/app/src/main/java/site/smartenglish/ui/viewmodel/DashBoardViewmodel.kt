package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
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
           // 使用coroutineScope并行执行所有数据获取任务
           kotlinx.coroutines.coroutineScope {
               val profileDeferred = async {
                   try {
                       userRepository.getProfile()
                   } catch (e: Exception) {
                       e.printStackTrace()
                       null
                   }
               }

               val learnedListDeferred = async {
                   try {
                       learnedRepository.getLearnedWordList()?.size ?: 0
                   } catch (e: Exception) {
                       e.printStackTrace()
                       0
                   }
               }

               val usingTimeDeferred = async {
                   try {
                       var time = 0
                       time += usingRepository.getUsingTime("learn") ?: 0
                       time += usingRepository.getUsingTime("review") ?: 0
                       time += usingRepository.getUsingTime("listen") ?: 0
                       time += usingRepository.getUsingTime("read") ?: 0
                       time
                   } catch (e: Exception) {
                       e.printStackTrace()
                       0
                   }
               }

               val todayLearnedCountDeferred = async {
                   try {
                       learnedRepository.getTodayLearnedWordCount()
                   } catch (e: Exception) {
                       e.printStackTrace()
                       0
                   }
               }

               val todayLearnedTimeDeferred = async {
                   try {
                       usingRepository.getTodayLearnTime()
                   } catch (e: Exception) {
                       e.printStackTrace()
                       0
                   }
               }

               // 等待所有结果并一次性更新UI
               val profile = profileDeferred.await()
               profile?.let {
                   val wordbook = it.wordbook ?: WordBook()

                   // 批量更新状态，减少UI重绘
                   _wordbookUrl.value = wordbook.cover ?: ""
                   _wordbookTotal.value = wordbook.wordcount ?: 0
                   _wordbookLearned.value = (wordbook.wordcount ?: 0) - (it.new_word_count ?: 0)
                   _currentWordBookId.value = it.wordbook?.id ?: 0
               }

               // 更新学习数据
               _totalWord.value = learnedListDeferred.await()
               _totalTime.value = usingTimeDeferred.await()
               _todayWord.value = todayLearnedCountDeferred.await()
               _todayTime.value = todayLearnedTimeDeferred.await()
           }
       }
   }
}