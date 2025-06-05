package site.smartenglish.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.remote.data.PutWordSetRequest
import site.smartenglish.remote.data.PutWordSetRequestElement
import site.smartenglish.repository.LearnedRepository
import site.smartenglish.repository.WordRepository
import site.smartenglish.repository.WordSetRepository
import javax.inject.Inject

// 单词列表中每个单词所需数据
data class LearnWordInfo(
    // 单词信息
    val word: GetWordResponse,
    // 相似词 四个
    val similarWords: List<GetWordResponse>,
    // 学习阶段
    val stage: Int,
    val wrongCount: Int = 0
)


@HiltViewModel
class LearnViewmodel @Inject constructor(
    private val wordRepository: WordRepository,
    private val wordSetRepository: WordSetRepository,
    private val learnedRepository: LearnedRepository
) : ViewModel() {
    private val _wordDetailList = MutableStateFlow<MutableList<LearnWordInfo>>(mutableListOf())
    val wordDetailList = _wordDetailList.asStateFlow()

    private val _learnedWordNum = MutableStateFlow(0)
    val learnedWordNum = _learnedWordNum.asStateFlow()

    private val _currentWordIndex = MutableStateFlow(0)
    val currentWordIndex = _currentWordIndex.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private suspend fun getWordDetail(word: String, stage: Int) {
        try {
            val wordInfo = wordRepository.getWordInfo(word)
            _wordDetailList.value.add(
                LearnWordInfo(
                    word = wordInfo,
                    similarWords = getSimilarWords(word).toMutableList().apply {
                        add(wordInfo) // 添加当前单词作为选项之一
                        shuffle() // 打乱选项顺序
                    },
                    stage = stage
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun getWordSetDetail() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = wordSetRepository.getWordSet("learn")
                list?.forEach {
                    getWordDetail(it?.word ?: "", it?.stage ?: 0)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getSimilarWords(word: String): List<GetWordResponse> {
        try {
            var q = word
            while (true) {
                val list = wordRepository.fuzzySearchWord(word)
                if (list.size >= 3) {
                    return list.take(3)
                } else q = q.dropLast(1) // 如果没有找到三个相似词，就去掉最后一个字母继续搜索
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }

    fun nextWord() {
        if (_currentWordIndex.value < _wordDetailList.value.size - 1) {
            _currentWordIndex.value += 1
        } else {
            _currentWordIndex.value = 0 // 当达到列表末尾时，回到列表开始
        }
    }

    fun passWord(index: Int) {
        if (index != -1) {
            val currentWord = _wordDetailList.value[index]
            val newStage = currentWord.stage + 1

            // 更新单词的stage
            _wordDetailList.value[index] = currentWord.copy(stage = newStage)

            // 如果stage达到4，上传单词并获取新词
            if (newStage == 4) {
                viewModelScope.launch {
                    try {
                        val response = learnedRepository.updateLearnedWordList(
                            word = currentWord.word.word ?: "",
                            reviewDate = java.time.LocalDate.now().plusDays(1).toString(),
                            status = "learn"
                        )
                        _learnedWordNum.value += 1
                        // 获取新词替换已完成的词
                        val newWord = response.new_word
                        newWord.let {
                            getWordDetail(it ?: "", 0)
                            // 移除已完成的单词
                            _wordDetailList.value.removeAt(index)
                        }
                        Log.d("LearnViewmodel", "单词 ${currentWord.word.word} 学习完成，已上传到已学单词列表, 新词${newWord}已添加")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    fun failWord(index: Int) {
        if (index != -1) {
            val currentWord = _wordDetailList.value[index]
            // 更新错题次数
            val newWrongCount = currentWord.wrongCount + 1
            _wordDetailList.value[index] = currentWord.copy(wrongCount = newWrongCount)

            // 如果错题次数超过5次，重置stage为0
            if (newWrongCount >= 5) {
                _wordDetailList.value[index] = currentWord.copy(stage = 0, wrongCount = 0)
            }
        }
    }

    //学到10个触发上传
    fun uploadLearnedWords() {
        viewModelScope.launch {
            if (_learnedWordNum.value >= 10) {
                try {
                    wordSetRepository.updateWordSet(
                        type = "learned",
                        putWordSetRequest = _wordDetailList.value.map {
                            PutWordSetRequestElement(
                                word = it.word.word ?: "",
                                stage = it.stage,
                            )
                        }
                    )
                    _learnedWordNum.value = 0 // 重置已学单词数量
                } catch (e: Exception) {
                    Log.e("LearnViewmodel", "上传已学单词失败: ${e.message}")
                }
            }
        }
    }


}