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
    private val dataStore:DataStoreManager
){
    suspend fun getUsingTime(name: String): Int? =
        api.getUsingTime(name).handleResponse("获取使用时间失败") as Int?

    suspend fun updateUsingTime(name: String, duration: Int): Boolean =
        api.updateUsingTime(PutUsingRequest(name, duration)).handleResponse("更新使用时间失败")
            .let { it == null }

//    suspend fun getTodayUsingTime(name: String): Int? =
//        api.getTodayUsingTime(name).handleResponse("获取今日使用时间失败") as Int?
//
//    suspend fun updateTodayUsingTime(name: String, duration: Int): Boolean =
//        api.updateTodayUsingTime(PutUsingRequest(name, duration)).handleResponse("更新今日使用时间失败")
//            .let { it == null }


}