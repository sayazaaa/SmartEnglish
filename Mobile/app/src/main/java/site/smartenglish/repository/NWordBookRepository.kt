package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.AddNWordBookWordRequest
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NWordBookRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun addNWord(word:String,nWordBookId:Int){
        api.addNWord(
            AddNWordBookWordRequest(word,nWordBookId)
        )
    }

    suspend fun checkNWordBook(word: String, nWordBookId: Int): Boolean {
        return api.checkNWordBook(word, nWordBookId).handleResponse("检查单词是否在生词本失败") as Boolean
    }

}