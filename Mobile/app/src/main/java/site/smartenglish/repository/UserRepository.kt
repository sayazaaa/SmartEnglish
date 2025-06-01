package site.smartenglish.repository

import site.smartenglish.manager.DataStoreManager
import site.smartenglish.manager.SessionManager
import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.ChangeProfileRequest
import site.smartenglish.remote.data.ChangeProfileResponse
import site.smartenglish.remote.data.GetProfileResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: ApiService,
    private val dataStoreManager: DataStoreManager,
    private val sessionManager: SessionManager,
    ) {

    /**
     * 获取用户资料
     *
     * @return 用户资料数据
     * @throws Exception 当请求失败时抛出异常
     */
    suspend fun getProfile(): GetProfileResponse {
        return api.getProfile().handleResponse("获取用户资料失败") as GetProfileResponse
    }

    /**
     * 修改用户个人信息
     *
     * @param name 用户名称
     * @param description 用户描述
     * @param avatar 用户头像URL
     * @param wordbookId 用户选择的词书ID
     * @return 更新后的用户资料
     * @throws Exception 当请求失败时抛出异常
     */
    suspend fun changeProfile(
        name: String? = null,
        description: String? = null,
        avatar: String? = null,
        wordbookId: Int? = null
    ): GetProfileResponse {
        val request = ChangeProfileRequest(
            name = name, description = description, avatar = avatar, wordbookid = wordbookId
        )
        val response =
            api.changeProfile(request).handleResponse("修改用户资料失败") as ChangeProfileResponse

        return GetProfileResponse(
            name = response.name,
            description = response.description,
            avatar = response.avatar,
            wordbook = response.wordbook
        )
    }

    //TODO feedback
}