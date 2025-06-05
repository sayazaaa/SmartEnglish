package site.smartenglish.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import site.smartenglish.remote.data.AddFavoritesRequest
import site.smartenglish.remote.data.AddNWordBookWordRequest
import site.smartenglish.remote.data.ChangePasswordRequest
import site.smartenglish.remote.data.ChangePasswordResponse
import site.smartenglish.remote.data.ChangeProfileRequest
import site.smartenglish.remote.data.ChangeProfileResponse
import site.smartenglish.remote.data.CreateFavoritesSetRequest
import site.smartenglish.remote.data.CreateNWordBookRequest
import site.smartenglish.remote.data.FeedBackRequest
import site.smartenglish.remote.data.GetArticleResponse
import site.smartenglish.remote.data.GetFavoritesListResponse
import site.smartenglish.remote.data.GetFavoritesSetListResponse
import site.smartenglish.remote.data.GetLearnedResponse
import site.smartenglish.remote.data.GetNWordBookListResponse
import site.smartenglish.remote.data.GetNWordBookWordListResponse
import site.smartenglish.remote.data.GetProfileResponse
import site.smartenglish.remote.data.GetUsingResponse
import site.smartenglish.remote.data.GetWordBookInfoResponse
import site.smartenglish.remote.data.GetWordBookListResponse
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.remote.data.GetWordSetResponse
import site.smartenglish.remote.data.LoginRequest
import site.smartenglish.remote.data.LoginResponse
import site.smartenglish.remote.data.PutLearnedRequest
import site.smartenglish.remote.data.PutLearnedResponse
import site.smartenglish.remote.data.PutUsingRequest
import site.smartenglish.remote.data.PutWordSetRequest
import site.smartenglish.remote.data.RegisterRequest
import site.smartenglish.remote.data.RegisterResponse
import site.smartenglish.remote.data.SearchArticleResponse
import site.smartenglish.remote.data.SearchWordResponse


interface ApiService {

    /**
     * 用户注册接口
     *
     * @param registerRequest
     * {
     *     "phone": "string",
     *     "verification": "string",
     *     "password": "string"
     * }
     *
     * @return
     * {
     *     "message": "string"
     * }
     */
    @POST("account")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    /**
     * 用户登录接口
     *
     * @param loginRequest
     * {
     *     "phone": "string",
     *     "verification": "string",
     *     "password": "string"
     * }
     * @return
     * {
     *     "message": "string"
     * }
     */
    @POST("account/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    /**
     * 修改密码接口
     *
     * @param changePasswordRequest
     * {
     *     "phone": "string",
     *     "verification": "string",
     *     "password": "string"
     * }
     * @return
     * {
     *     "message": "string"
     * }
     */
    @PUT("account")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ): Response<ChangePasswordResponse>

    /**
     * 修改用户个人信息接口
     *
     * @param changeProfileRequest
     * {
     *     "name": "string",
     *     "description": "string",
     *     "avatar": "string",
     *     "wordbookid": 0
     * }
     * @return
     * {
     *     "name": "string",
     *     "description": "string",
     *     "avatar": "string",
     *     "wordbook": {
     *         "id": 0,
     *         "name": "string",
     *         "cover": "string",
     *         "wordcount": 0
     *     },
     *     "new_word_count": 0,
     *     "today_review": 0
     * }
     */
    @PUT("user")
    suspend fun changeProfile(
        @Body changeProfileRequest: ChangeProfileRequest
    ): Response<ChangeProfileResponse>

    /**
     * 获取用户资料接口
     *
     * @return
     * {
     *     "name": "string",
     *     "description": "string",
     *     "avatar": "string",
     *     "wordbook": {
     *         "id": 0,
     *         "name": "string",
     *         "cover": "string",
     *         "wordcount": 0
     *     },
     *     "new_word_count": 0,
     *     "today_review": 0
     * }
     */
    @GET("user")
    suspend fun getProfile(): Response<GetProfileResponse>

    /**
     * 获取单词详细信息接口
     *
     * @param word
     * @return
     * {
     *     "word": "string",
     *     "phonetic": "string",
     *     "pronunciation": "string",
     *     "explanations": [
     *         "string"
     *     ],
     *     "synonyms": {
     *         "a": [
     *             "string"
     *         ],
     *         "v": [
     *             "string"
     *         ],
     *         "n": [
     *             "string"
     *         ]
     *     },
     *     "antonyms": {
     *         "a": [
     *             "string"
     *         ],
     *         "v": [
     *             "string"
     *         ],
     *         "n": [
     *             "string"
     *         ]
     *     },
     *     "examples": [
     *         {
     *             "english": "string",
     *             "chinese": "string",
     *             "audio": "string"
     *         }
     *     ]
     * }
     */
    @GET("word")
    suspend fun getWordInfo(
        @Query("word") word: String
    ): Response<GetWordResponse>
    /**
     * 搜索单词接口
     *
     * @param word 搜索的关键词或单词
     * @return
     * [
     *     {
     *         "word": "string",
     *         "explanations": [
     *             "string"
     *         ]
     *     }
     * ]
     */
    @GET("word/search")
    suspend fun searchWord(
        @Query("query_string") word: String,
    ): Response<SearchWordResponse>

    /**
     * 模糊搜索单词接口
     *
     * @param word 搜索的关键词或单词
     * @return
     */
    @GET("/word/match")
    suspend fun fuzzySearchWord(
        @Query("word") word: String,
    ): Response<List<GetWordResponse>>

    /**
     * 获取单词组接口
     *
     * @param type "learn"/"review"对应不同的单词组
     * @return
     * [
     *     {
     *         "word": "string",
     *         "stage": 0
     *     }
     * ]
     */
    @GET("wordset")
    suspend fun getWordSet(
        @Query("type") type: String
    ): Response<GetWordSetResponse>

    /**
     * 提交新单词组接口
     * @param type "learn"/"review"对应不同的单词组
     * @param wordSet
     * [
     *     {
     *         "word": "string",
     *         "stage": 0
     *     }
     * ]
     * @return 操作结果的响应
     */
    @PUT("wordset")
    suspend fun putWordSet(
        @Query("type") type: String,
        @Body wordSet: PutWordSetRequest
    ): Response<Unit>

    /**
     * 获取词书信息接口
     *
     * @param wordbookId 词书的ID
     * @return
     * [
     *     "string"
     * ]
     */
    @GET("wordbook/detail")
    suspend fun getWordBookInfo(
        @Query("id") wordbookId: Int
    ): Response<GetWordBookInfoResponse>

    /**
     * 获取词书列表接口
     *
     * @return
     * [
     *     {
     *         "id": 0,
     *         "name": "string",
     *         "cover": "string",
     *         "wordcount": 0
     *     }
     * ]
     */
    @GET("wordbook")
    suspend fun getWordBookList(): Response<GetWordBookListResponse>

    /**
     * 获取模块使用时长接口
     *
     * @param modName 模块名称："learn"/"review"/"listen"/"read"
     * @return
     * 返回该模块的累计使用时长，单位为分钟
     */
    @GET("using")
    suspend fun getUsingTime(
        @Query("modname") modName: String
    ): Response<GetUsingResponse>

    /**
     * 更新应用使用时间接口
     *
     * @param usingTime
     * - function "learn"/"review"/"listen"/"read"
     * - duration 使用时长，单位为分钟
     * {
     *     "function": "string",
     *     "duration": 0
     * }
     * @return 操作结果的响应
     */
    @PUT("using")
    suspend fun updateUsingTime(
        @Body usingTime: PutUsingRequest
    ): Response<Unit>

    /**
     * 获取已学习列表接口
     *
     * @return
     * [
     *     "string"
     * ]
     */
    @GET("learned")
    suspend fun getLearnedWords(
    ): Response<GetLearnedResponse>

    /**
     * 更新已学习列表接口
     *
     * @param learnedWord
     * {
     *     "word": "string",
     *     "review_date": "string",
     *     "status": "string"
     * }
     * @return
     * {
     *     "new_word": "string",
     *     "message": "string"
     * }
     */
    @PUT("learned")
    suspend fun updateLearnedWords(
        @Body learnedWord: PutLearnedRequest
    ): Response<PutLearnedResponse>

    /**
     * 获取生词本单词列表接口
     *
     * @param nWordBookId 生词本的ID
     * @return
     * [
     *     "string"
     * ]
     */
    @GET("nwordbook")
    suspend fun getNWordBookWordList(
        @Query("id") nWordBookId: Int,
    ): Response<GetNWordBookWordListResponse>

    /**
     * 获取生词本列表接口
     *
     * @return
     * [
     *     {
     *         "id": 0,
     *         "name": "string",
     *         "cover": "string"
     *     }
     * ]
     */
    @GET("nwordbooklist")
    suspend fun getNWordBookList(): Response<GetNWordBookListResponse>

    /**
     * 创建生词本接口
     *
     * @param nWordBookInfo
     * {
     *     "name": "string",
     *     "cover": "string"
     * }
     */
    @POST("nwordbook")
    suspend fun createNWordBook(
        @Body nWordBookInfo: CreateNWordBookRequest
    ): Response<Unit>

    /**
     * 向生词本添加/删除单词接口
     *
     * @param nWordInfo
     * - method "add"/"remove"
     * {
     *     "word": "string",
     *     "nwordbook": 0,
     *     "method": "string"
     * }
     */
    @PUT("nwordbook")
    suspend fun addNWord(
        @Body nWordInfo: AddNWordBookWordRequest
    ): Response<Unit>

    /**
     * 获取文章详情接口
     *
     * @param article 文章的ID
     * @return
     * {
     *     "id": "string",
     *     "title": "string",
     *     "cover": "string",
     *     "content": "string",
     *     "date": "string",
     *     "tags": [
     *         "string"
     *     ]
     * }
     */
    @GET("article")
    suspend fun getArticle(
        @Query("id") article: String,
    ): Response<GetArticleResponse>

    /**
     * 搜索文章接口
     *
     * @param article 搜索的关键词
     * @return
     * [
     *     {
     *         "id": "string",
     *         "title": "string",
     *         "cover": "string",
     *         "date": "string",
     *         "tags": [
     *             "string"
     *         ]
     *     }
     * ]
     */
    @GET("article/search")
    suspend fun searchArticle(
        @Query("query_string") article: String,
    ): Response<SearchArticleResponse>

    /**
     * 获取收藏夹列表接口
     *
     * @return
     * [
     *     {
     *         "id": 0,
     *         "name": "string",
     *         "cover": "string"
     *     }
     * ]
     */
    @GET("favorite")
    suspend fun getFavoriteSetList(): Response<GetFavoritesSetListResponse>

    /**
     * 获取收藏夹内容接口
     *
     * @param favoriteSet 收藏集的ID
     * @return
     * [
     *     {
     *         "id": "string",
     *         "title": "string",
     *         "cover": "string",
     *         "date": "string",
     *         "tags": [
     *             "string"
     *         ]
     *     }
     * ]
     */
    @GET("favorite/detail")
    suspend fun getFavoritesInfo(
        @Query("id") favoriteSet: Int,
    ): Response<GetFavoritesListResponse>

    /**
     * 新建收藏夹接口
     *
     * @param favoriteSetInfo
     * {
     *     "name": "string",
     *     "cover": "string"
     * }
     * @return 创建结果的响应
     */
    @POST("favorite")
    suspend fun addFavoritesSet(
        @Body favoriteSetInfo: CreateFavoritesSetRequest
    ): Response<Unit>

    /**
     * 收藏夹添加/删除文章接口
     *
     * @param favoritesInfo
     * - method "add"/"remove"
     * {
     *     "favorite_set": 0,
     *     "article": "string",
     *     "method": "string"
     * }
     * @return 添加结果的响应
     */
    @PUT("favorite")
    suspend fun addFavorites(
        @Body favoritesInfo: AddFavoritesRequest
    ): Response<Unit>

    /**
     * 删除收藏夹接口
     *
     * @param favoriteSet 要删除的收藏集ID
     */
    @DELETE("favorite")
    suspend fun deleteFavorites(
        @Query("id") favoriteSet: Int
    ): Response<Unit>

    /**
     * 检查文章是否在收藏夹中
     *
     * @param favoriteSet 收藏集的ID
     * @param article 文章的ID
     * @return 如果文章在收藏夹中，返回true；否则返回false
     */
    @GET("favorite/check")
    suspend fun checkFavorite(
        @Query("id") favoriteSet: Int,
        @Query("article") article: String
    ): Response<Boolean?>

    /**
     * 反馈接口
     *
     * @param feedback 反馈内容
     */
    @POST("feedback")
    suspend fun sendFeedback(
        @Body feedback: FeedBackRequest
    ): Response<Unit>
}
