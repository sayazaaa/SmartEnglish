package site.smartenglish.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetArticleResponse
import site.smartenglish.remote.data.SearchArticleResponse
import site.smartenglish.remote.data.SearchArticleResponseElement
import site.smartenglish.repository.ArticleRepository
import javax.inject.Inject

@HiltViewModel
class ArticleViewmodel @Inject constructor(
    private val articleRepository: ArticleRepository
) :ViewModel(){
    private val _articleList = MutableStateFlow<SearchArticleResponse>(emptyList())
    val articleList = _articleList.asStateFlow()

    private val _searchResult = MutableStateFlow<SearchArticleResponse>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private val _articleDetail = MutableStateFlow<GetArticleResponse>(
        GetArticleResponse(
            content = null,
            cover = null,
            date = null,
            id = null,
            tags = emptyList(),
            title = null
        )
    )

    init {
        getArticleList()
    }

    fun getArticleList() {
        viewModelScope.launch {
            try {
                _articleList.value = articleRepository.getArticleList()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getArticleById(id: String){
        viewModelScope.launch {
            try {
                _articleDetail.value = articleRepository.getArticle(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearArticleDetail() {
        _articleDetail.value = GetArticleResponse(
            content = null,
            cover = null,
            date = null,
            id = null,
            tags = emptyList(),
            title = null
        )
    }

    fun searchArticle(keyword: String) {
        viewModelScope.launch {
            try {
                _searchResult.value = articleRepository.searchArticle(keyword)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearSearchResult() {
        _searchResult.value = emptyList()
    }




}