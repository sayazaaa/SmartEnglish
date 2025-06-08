package site.smartenglish.repository

import site.smartenglish.manager.DataStoreManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.GetLearnedResponse
import site.smartenglish.remote.data.PutLearnedRequest
import site.smartenglish.remote.data.PutLearnedResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnedRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager
){
    suspend fun getLearnedWordList(): GetLearnedResponse {
        return api.getLearnedWords().handleResponse("获取已学单词失败") as GetLearnedResponse
    }

    suspend fun updateLearnedWord(
        word: String,
        reviewDate: String,
        status: String
    ): PutLearnedResponse {
        val response = api.updateLearnedWords(PutLearnedRequest(word, reviewDate, status))
            .handleResponse("更新已学单词失败") as PutLearnedResponse
        if (status == "learn") dataStoreManager.addTodayLearnNum(1)
        return  response
    }

    suspend fun getTodayLearnedWordCount(): Int {
        return dataStoreManager.getTodayLearnNumSync()
    }
}