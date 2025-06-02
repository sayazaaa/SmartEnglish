package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.remote.data.SearchWordResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getWordInfo(word:String): GetWordResponse {
        return api.getWordInfo(word).handleResponse("获取单词信息失败") as GetWordResponse
    }

    suspend fun searchWord(word: String): SearchWordResponse {
        return api.searchWord(word).handleResponse("搜索单词失败") as SearchWordResponse
    }
}