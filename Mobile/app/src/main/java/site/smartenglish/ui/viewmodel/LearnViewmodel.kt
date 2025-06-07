package site.smartenglish.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.remote.data.GetWordSetResponse
import site.smartenglish.remote.data.PutWordSetRequestElement
import site.smartenglish.repository.LearnedRepository
import site.smartenglish.repository.WordRepository
import site.smartenglish.repository.WordSetRepository
import javax.inject.Inject

// 单词列表中每个单词所需数据
data class LearnWordInfo(
    // 单词信息
    val word: GetWordResponse = GetWordResponse(),
    // 相似词 四个
    val similarWords: List<GetWordResponse> = emptyList(),
    // 学习阶段
    val stage: Int = 0,
    val wrongCount: Int = 0
)


@HiltViewModel
class LearnViewmodel @Inject constructor(
    private val wordRepository: WordRepository,
    private val wordSetRepository: WordSetRepository,
    private val learnedRepository: LearnedRepository,

) : ViewModel() {
    private val wordDetailList = MutableList<LearnWordInfo>(0) { LearnWordInfo() }
    private val learnedList = MutableList<String>(0) { "" }
    private var currentWordIndex = 0

    private val _wordDetail = MutableStateFlow<LearnWordInfo>(LearnWordInfo())
    val wordDetail = _wordDetail.asStateFlow()

    private val _learnedWordNum = MutableStateFlow(0)
    val learnedWordNum = _learnedWordNum.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _navigateBackSelection = MutableStateFlow(false)
    val navigateBackSelection = _navigateBackSelection.asStateFlow()

    private val _snackBar = MutableStateFlow("")
    val snackBar = _snackBar.asStateFlow()

    private  val _navigateToFinish = MutableStateFlow(false)
    val navigateToFinish = _navigateToFinish.asStateFlow()

    private suspend fun getWordDetail(word: String, stage: Int) {
        try {
            val wordInfo = wordRepository.getWordInfo(word)
            wordDetailList.add(
                LearnWordInfo(
                    word = wordInfo,
                    similarWords = getSimilarWords(word).toMutableList().apply {
                        add(wordInfo) // 添加当前单词作为选项之一
                        shuffle() // 打乱选项顺序
                        Log.d("LearnViewmodel", "获取相似词: ${this.joinToString { it.word ?: "" }}")
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
                if (list?.isEmpty() == true) {
                    _snackBar.value = "词书中没有可学单词！"
                    _navigateBackSelection.value = true
                    return@launch
                }
                list?.forEachIndexed { index, it ->
                    getWordDetail(it?.word ?: "", it?.stage ?: 0)
                    if (index == 0) {
                        // 初始化第一个单词详情
                        _wordDetail.value = wordDetailList[currentWordIndex]
                        _isLoading.value = false
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
        val currentIndex = currentWordIndex

        // 如果当前列表为空，直接返回
        if (wordDetailList.isEmpty()) {
            return
        }

        // 获取当前单词信息
        val currentWord = wordDetailList[currentIndex]
        val newStage = currentWord.stage

        // 更新当前单词信息
        _wordDetail.value = run {
            // 过滤掉当前单词和stage为4的单词
            val availableWords = wordDetailList.filterIndexed { index, wordInfo ->
                index != currentIndex && wordInfo.stage < 4
            }

            // 如果没有可用单词，则返回当前单词
            if (availableWords.isEmpty()) {
                return@run wordDetailList[currentIndex]
            }

            // 根据stage计算权重
            val weights = availableWords.mapIndexed { i, wordInfo ->
                // stage越高权重越大，增加出现概率
                val weight = (wordInfo.stage + 1) * (wordInfo.stage + 1) // 平方增长以加强差异
                i to weight.toDouble()
            }

            // 计算总权重
            val totalWeight = weights.sumOf { it.second }
            // 生成0到totalWeight之间的随机数
            val random = Math.random() * totalWeight
            // 根据权重随机选择单词
            var cumulativeWeight = 0.0
            var selectedIndex = 0 // 默认选第一个

            for ((i, weight) in weights) {
                cumulativeWeight += weight
                if (random <= cumulativeWeight) {
                    // 选中当前索引
                    selectedIndex = i
                    break
                }
            }

            // 找到选中单词在原列表中的索引
            currentWordIndex = wordDetailList.indexOf(availableWords[selectedIndex])
            wordDetailList[currentWordIndex]
        }
        // 如果stage达到4，上传单词并获取新词
        if (newStage == 4) {
            viewModelScope.launch {
                uploadWord(0,currentWord)
            }
        }
    }

    private suspend fun uploadWord(retry:Int,currentWord: LearnWordInfo) {
        if (retry>3) {
            _snackBar.value = "网络连接失败，请检查网络后重试"
            _navigateBackSelection.value = true
            return
        }
        try {
            // 使用当前索引获取当前单词
            _isLoading.value = true
            val response = learnedRepository.updateLearnedWord(
                word = currentWord.word.word ?: "",
                reviewDate = java.time.LocalDate.now().plusDays(1).toString(),
                status = "learn"
            )
            learnedList.add(currentWord.word.word ?: "")
            _learnedWordNum.value += 1
            Log.d("LearnViewmodel", "message: ${response.message}, new_word: ${response.new_word}")
            if (!response.new_word.isNullOrBlank()) {
                // 添加新单词到学习列表
                getWordDetail(response.new_word, 0)
                // 从列表中移除当前已学完的单词
                wordDetailList.removeIf { it.word.word == currentWord.word.word }
                Log.d(
                    "LearnViewmodel",
                    "上传单词${currentWord.word.word}成功，添加新单词: ${response.new_word}"
                )

                if (_learnedWordNum.value == 10) {
                    uploadLearnedWords()
                    _navigateToFinish.value = true
                }
            } else {
                wordDetailList.removeIf { it.word.word == currentWord.word.word }
                Log.d("LearnViewmodel", "没有新单词")
                if (wordDetailList.size == 0) {
                    // 如果列表为空，触发返回操作
                    _navigateToFinish.value = true
                    _snackBar.value = "恭喜你，已学完词书所有单词！"
                    return
                }
                if (_learnedWordNum.value == 10) {
                    uploadLearnedWords()
                    _navigateToFinish.value = true
                }
            }


        } catch (e: Exception) {
            Log.e("LearnViewmodel", "单词上传失败: ${e.message}")
            // 上传失败，从服务器获取最新的 wordset 并进行
            var retryCount = 0
            var serverWordSet: GetWordSetResponse = emptyList()
            var success = false

            // 尝试最多3次获取
            while (retryCount < 3 && !success) {
                try {
                    serverWordSet = wordSetRepository.getWordSet("learn")
                    success = true
                } catch (e: Exception) {
                    retryCount++
                    Log.w("LearnViewmodel", "获取词库失败，重试第${retryCount}次: ${e.message}")
                    if (retryCount < 3) {
                        // 延迟一段时间再重试
                        kotlinx.coroutines.delay(1000)
                    } else {
                        // 三次都失败，显示提示
                        _snackBar.value = "网络连接失败，请检查网络后重试"
                        _navigateBackSelection.value = true
                        throw e
                    }
                }
            }

            // 构建本地单词映射，方便查找
            val localWordMap = wordDetailList.associateBy { it.word.word }

            // 检查服务器单词列表和本地单词列表是否一致
            val serverWords = serverWordSet?.map { it?.word }?.toSet()
            val localWords = localWordMap.keys.toSet()

            if (serverWords == localWords) {
                // 如果单词列表一致，只需重试上传当前单词
                uploadWord(retry+1,currentWord)
            } else {
                // 单词列表不一致，需要重建列表
                // 清空当前列表，准备重建
                wordDetailList.clear()

                // 处理服务器返回的 wordSet
                serverWordSet?.forEach { serverWord ->
                    val word = serverWord?.word
                    val serverStage = serverWord?.stage ?: 0

                    if (word != null) {
                        // 检查本地是否存在该单词
                        val localWord = localWordMap[word]
                        if (localWord != null) {
                            // 本地存在，使用本地的 stage（可能更新过）
                            wordDetailList.add(localWord)
                        } else {
                            // 本地不存在，使用服务器的 stage
                            try {
                                getWordDetail(word, serverStage)
                            } catch (e: Exception) {
                                Log.e("LearnViewmodel", "同步单词详情失败: ${e.message}")
                            }
                        }
                    }
                }
                learnedList.add(currentWord.word.word ?: "")
                _learnedWordNum.value += 1

                try {
                    wordSetRepository.updateWordSet(
                        type = "learn",
                        putWordSetRequest = wordDetailList.map {
                            PutWordSetRequestElement(
                                word = it.word.word ?: "",
                                stage = it.stage,
                            )
                        }
                    )
                }catch (e: Exception) {
                    Log.e("LearnViewmodel", "上传词库失败: ${e.message}")
                }
            }
        }finally {
            _isLoading.value = false
        }
    }




    fun passWord() {
        val currentWord = wordDetailList[currentWordIndex]
        val newStage = currentWord.stage + 1
        //shuffle相似词
        val shuffledSimilarWords = currentWord.similarWords.shuffled()
        // 更新单词的stage
        wordDetailList[currentWordIndex] =
            currentWord.copy(stage = newStage, similarWords = shuffledSimilarWords)

    }

    fun failWord() {
        val currentWord = wordDetailList[currentWordIndex]

        // 更新错题次数
        val newWrongCount = currentWord.wrongCount + 1

        //shuffle相似词
        val shuffledSimilarWords = currentWord.similarWords.shuffled()

        wordDetailList[currentWordIndex] =
            currentWord.copy(wrongCount = newWrongCount, similarWords = shuffledSimilarWords)

    }

    //学到10个触发上传
    fun uploadLearnedWords() {
        viewModelScope.launch {
            if (_learnedWordNum.value >= 10) {
                try {
                    wordSetRepository.updateWordSet(
                        type = "learn", putWordSetRequest = wordDetailList.map {
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

    fun clearSnackBar() {
        _snackBar.value = ""
    }

    fun getLearnedWordList(): List<String> {
        return learnedList
    }


}