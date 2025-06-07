package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.AddNWordBookWordRequest
import site.smartenglish.remote.data.CreateNWordBookRequest
import site.smartenglish.remote.data.GetNWordBookListResponse
import site.smartenglish.remote.data.GetNWordBookWordListResponse
import site.smartenglish.remote.data.GetWordBookListResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NWordBookRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun addNWord(word:String,nWordBookId:Int){
        api.addNWord(
            AddNWordBookWordRequest(word,nWordBookId,"add")
        )
    }

    suspend fun removeNWord(word: String, nWordBookId: Int) {
        api.addNWord(
            AddNWordBookWordRequest(word,nWordBookId,"delete")
        )
    }

    suspend fun checkNWordBook(word: String, nWordBookId: Int): Boolean {
        return api.checkNWordBook(word, nWordBookId).handleResponse("检查单词是否在生词本失败") as Boolean
    }

    suspend fun getNWordBookList(): GetNWordBookListResponse {
        return api.getNWordBookList().handleResponse("获取生词本列表失败") as GetNWordBookListResponse
    }

    suspend fun  getNWordBookWords(nWordBookId: Int): GetNWordBookWordListResponse {
        return api.getNWordBookWordList(nWordBookId).handleResponse("获取生词本单词失败") as GetNWordBookWordListResponse
    }

    suspend fun createNWordBook(name: String,cover : String): Boolean {
        return api.createNWordBook(CreateNWordBookRequest(name,cover)).handleResponse("创建生词本失败").let {
            it != null
        }
    }




}