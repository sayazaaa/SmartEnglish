package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.GetLearnedResponse
import site.smartenglish.remote.data.PutLearnedRequest
import site.smartenglish.remote.data.PutLearnedResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnedRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getLearnedWordList(): GetLearnedResponse {
        return api.getLearnedWords().handleResponse("获取已学单词失败") as GetLearnedResponse
    }

    suspend fun updateLearnedWord(
        word: String,
        reviewDate: String,
        status: String
    ): PutLearnedResponse = api.updateLearnedWords(PutLearnedRequest(word, reviewDate, status))
        .handleResponse("更新已学单词失败") as PutLearnedResponse

}