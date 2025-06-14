package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.Get20NewWordResponse
import site.smartenglish.remote.data.GetWordBookInfoResponse
import site.smartenglish.remote.data.GetWordBookListResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordBookRepository @Inject constructor(
    private val api: ApiService
){
    // 获取词书列表
    suspend fun getWordBooks(): GetWordBookListResponse {
        return api.getWordBookList().handleResponse("获取词书列表失败") as GetWordBookListResponse
    }

    // 获取词书详情
    suspend fun getWordBookDetail(id: Int): GetWordBookInfoResponse{
        return api.getWordBookInfo(id).handleResponse("获取词书详情失败") as GetWordBookInfoResponse
    }

    // 获取20个新单词
    suspend fun get20NewWords(): List<String?>? {
        return api.get20NewWord().handleResponse("获取20个新单词失败") as Get20NewWordResponse
    }

}