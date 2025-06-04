package site.smartenglish.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.TencentCloudService
import site.smartenglish.remote.data.GetArticleResponse
import site.smartenglish.repository.ArticleRepository
import javax.inject.Inject

@HiltViewModel
class ArticleDetailViewmodel @Inject constructor(
    private val articleRepository: ArticleRepository
) : ViewModel() {

    private val _articleDetail = MutableStateFlow(GetArticleResponse())
    val articleDetail = _articleDetail.asStateFlow()

    fun getArticleById(id: String) {
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
            content = null, cover = null, date = null, id = null, tags = emptyList(), title = null
        )
    }

}