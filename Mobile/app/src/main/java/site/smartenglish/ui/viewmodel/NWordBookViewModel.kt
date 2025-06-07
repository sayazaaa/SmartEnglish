package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetNWordBookListResponse
import site.smartenglish.repository.NWordBookRepository
import javax.inject.Inject

@HiltViewModel
class NWordBookViewmodel @Inject constructor(
    private val nWordBookRepository: NWordBookRepository
) : ViewModel() {
    //总收藏
    private val _isAnyNWordBook = MutableStateFlow(false)
    val isAnyNWordBook = _isAnyNWordBook.asStateFlow()

    //收藏细则（？ Map<word,Map<SetId, isFav>>
    private val _isNWordBook = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val isNWordBook = _isNWordBook.asStateFlow()

    private val _nWordBookList = MutableStateFlow<GetNWordBookListResponse>(emptyList())
    val nWordBookList = _nWordBookList.asStateFlow()

    // Map<SetId,List<NWordBookListItemData>>
    private val _nWordBookDetail =
        MutableStateFlow<Map<Int, List<String>>>(emptyMap())
    val nWordBookDetail = _nWordBookDetail.asStateFlow()


    fun getIsNWordBook(word: String) {
        viewModelScope.launch {
            try {
                // 获取收藏夹列表
                val nWordBooksList = nWordBookRepository.getNWordBookList()
                _nWordBookList.value = nWordBooksList

                // 并行检查每个收藏夹
                val checkTasks = nWordBooksList?.filterNotNull()?.filter { it.id != null }?.map { set ->
                    async {
                        val isFav = nWordBookRepository.checkNWordBook(word, set.id!!)
                        set.id to isFav
                    }
                } ?: emptyList()

                // 等待所有检查完成并收集结果
                val results = checkTasks.awaitAll().toMap()

                // 更新状态
                _isNWordBook.value = results
                _isAnyNWordBook.value = results.values.any { it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getNWordBookList() {
        viewModelScope.launch {
            try {
                _nWordBookList.value = nWordBookRepository.getNWordBookList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addToNWordBook(word: String, nWordBookSetId: Int) {
        viewModelScope.launch {
            try {
                // 乐观更新UI状态
                val currentMap = _isNWordBook.value.toMutableMap()
                currentMap[nWordBookSetId] = true
                _isNWordBook.value = currentMap
                _isAnyNWordBook.value = true

                // 后台执行实际的网络请求
                nWordBookRepository.addNWord(word, nWordBookSetId)
                // 请求完成后再验证一次
                getIsNWordBook(word)
            } catch (e: Exception) {
                e.printStackTrace()
                // 发生错误时恢复状态
                getIsNWordBook(word)
            }
        }
    }

    fun removeFromNWordBook(word: String, nWordBookSetId: Int) {
        viewModelScope.launch {
            try {
                // 乐观更新UI状态
                val currentMap = _isNWordBook.value.toMutableMap()
                currentMap[nWordBookSetId] = false
                _isNWordBook.value = currentMap
                _isAnyNWordBook.value = _isNWordBook.value.values.any { it }

                // 后台执行实际的网络请求
                nWordBookRepository.removeNWord(word, nWordBookSetId)
                getIsNWordBook(word)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createNWordBookSet(name: String, cover: String = ""): kotlinx.coroutines.Job {
        return viewModelScope.launch {
            try {
                val result = nWordBookRepository.createNWordBook(name, cover)
                if (result) {
                    // 创建成功后自动刷新收藏夹列表
                    getNWordBookList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun deleteNWordBookSet(nWordBookSetId: Int): kotlinx.coroutines.Job  {
//        return viewModelScope.launch {
//            try {
//                val result = nWordBookRepository.deleteNWordBookSetById(nWordBookSetId)
//                if (result) {
//                    // 删除成功后刷新收藏夹列表
//                    getNWordBookList()
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

    fun getNWordBookDetail() {
        viewModelScope.launch {
            try {
                // 先获取收藏夹列表
                val nWordBooksList = nWordBookRepository.getNWordBookList()

                // 并行获取每个收藏夹的详情
                val detailTasks =
                    nWordBooksList?.filterNotNull()?.filter { it.id != null }?.map { set ->
                        async {
                            val articles = nWordBookRepository.getNWordBookWords(set.id!!)
                            set.id to articles
                        }
                    } ?: emptyList()

                // 等待所有请求完成并收集结果
                val results = detailTasks.awaitAll().toMap()

                // 更新状态
                _nWordBookDetail.value = results.mapValues { entry ->
                    entry.value?.map {
                        it ?: ""
                    }?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}