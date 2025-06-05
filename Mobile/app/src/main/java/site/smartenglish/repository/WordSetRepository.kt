package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.GetWordSetResponse
import site.smartenglish.remote.data.PutWordSetRequest
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordSetRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getWordSet(type:String): GetWordSetResponse {
        return api.getWordSet(type).handleResponse("获取单词组失败") as GetWordSetResponse
    }

    suspend fun updateWordSet(type: String,putWordSetRequest: PutWordSetRequest)
    : Boolean{
        return api.putWordSet(type,putWordSetRequest).handleResponse("更新单词组失败").let {
            it != null
        }
    }


}