package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.GetArticleResponse
import site.smartenglish.remote.data.SearchArticleResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getArticleList(): SearchArticleResponse {
        return api.searchArticle("").handleResponse("获取文章列表失败") as SearchArticleResponse
    }

    suspend fun getArticle(id: String): GetArticleResponse =
        api.getArticle(id).handleResponse("获取文章失败") as GetArticleResponse

    suspend fun searchArticle(keyword: String): SearchArticleResponse {
        return api.searchArticle(keyword).handleResponse("搜索文章失败") as SearchArticleResponse
    }

}