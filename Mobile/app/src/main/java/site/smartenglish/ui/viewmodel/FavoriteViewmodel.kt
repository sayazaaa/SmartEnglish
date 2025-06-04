package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetFavoritesSetListResponse
import site.smartenglish.repository.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

@HiltViewModel
class FavoriteViewmodel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    //总收藏
    private val _isAnyFavorite = MutableStateFlow(false)
    val isAnyFavorite = _isAnyFavorite.asStateFlow()

    //收藏细则（？ Map<SetId, isFav>
    private val _isFavorite = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val isFavorite = _isFavorite.asStateFlow()

    private val _favoritesList = MutableStateFlow<GetFavoritesSetListResponse>(emptyList())
    val favoritesList = _favoritesList.asStateFlow()

    fun getIsFavorite(articleId: String) {
        viewModelScope.launch {
            try {
                // 获取收藏夹列表
                val favoritesList = favoritesRepository.getFavoriteList()
                _favoritesList.value = favoritesList

                // 并行检查每个收藏夹
                val checkTasks = favoritesList?.filterNotNull()?.filter { it.id != null }?.map { set ->
                    async {
                        val isFav = favoritesRepository.checkIfFavorite(set.id!!, articleId)
                        set.id to isFav
                    }
                } ?: emptyList()

                // 等待所有检查完成并收集结果
                val results = checkTasks.awaitAll().toMap()

                // 更新状态
                _isFavorite.value = results
                _isAnyFavorite.value = results.values.any { it }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getFavoriteList() {
        try {
            _favoritesList.value = favoritesRepository.getFavoriteList()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun addToFavorite(articleId: String, favoriteSetId: Int) {
        viewModelScope.launch {
            try {
                // 乐观更新UI状态
                val currentMap = _isFavorite.value.toMutableMap()
                currentMap[favoriteSetId] = true
                _isFavorite.value = currentMap
                _isAnyFavorite.value = true

                // 后台执行实际的网络请求
                favoritesRepository.addToFavorite(favoriteSetId, articleId)
                // 请求完成后再验证一次
                getIsFavorite(articleId)
            } catch (e: Exception) {
                e.printStackTrace()
                // 发生错误时恢复状态
                getIsFavorite(articleId)
            }
        }
    }

    fun removeFromFavorite(articleId: String, favoriteSetId: Int) {
        viewModelScope.launch {
            try {
                // 乐观更新UI状态
                val currentMap = _isFavorite.value.toMutableMap()
                currentMap[favoriteSetId] = false
                _isFavorite.value = currentMap
                _isAnyFavorite.value = _isFavorite.value.values.any { it }

                // 后台执行实际的网络请求
                favoritesRepository.deleteFavorite(favoriteSetId, articleId)
                getIsFavorite(articleId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createFavoriteSet(title: String, cover: String = ""): kotlinx.coroutines.Job {
        return viewModelScope.launch {
            try {
                val result = favoritesRepository.createFavoriteSet(title, cover)
                if (result) {
                    // 创建成功后自动刷新收藏夹列表
                    getFavoriteList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteFavoriteSet(favoriteSetId: Int): kotlinx.coroutines.Job  {
        return viewModelScope.launch {
            try {
                val result = favoritesRepository.deleteFavoriteSetById(favoriteSetId)
                if (result) {
                    // 删除成功后刷新收藏夹列表
                    getFavoriteList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}