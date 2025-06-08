package site.smartenglish.repository

import site.smartenglish.manager.DataStoreManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.PutUsingRequest
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsingRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager
){
    suspend fun getUsingTime(name: String): Int?{
        val response = api.getUsingTime(name).handleResponse("获取使用时间失败") as Int?
        return response
    }


    suspend fun updateUsingTime(name: String, duration: Int): Boolean{
        val response = api.updateUsingTime(PutUsingRequest(name, duration)).handleResponse("更新使用时间失败")
            .let { it == null }
        dataStoreManager.addTodayLearnTime(duration)
        return response
    }

    suspend fun getTodayLearnTime(): Int {
        return dataStoreManager.getTodayLearnTimeSync()
    }


}