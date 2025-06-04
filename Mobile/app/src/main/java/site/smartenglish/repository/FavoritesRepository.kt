package site.smartenglish.repository

import site.smartenglish.remote.ApiService
import site.smartenglish.remote.data.AddFavoritesRequest
import site.smartenglish.remote.data.CreateFavoritesSetRequest
import site.smartenglish.remote.data.GetFavoritesListResponse
import site.smartenglish.remote.data.GetFavoritesSetListResponse
import site.smartenglish.util.handleResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesRepository @Inject constructor(
    private val api: ApiService
){
    suspend fun getFavoriteList(): GetFavoritesSetListResponse {
        return api.getFavoriteSetList()
            .handleResponse("获取收藏列表失败") as GetFavoritesSetListResponse
    }

    suspend fun getFavoriteDetail(id: Int): GetFavoritesListResponse {
        return api.getFavoritesInfo(id)
            .handleResponse("获取收藏详情失败") as GetFavoritesListResponse
    }

    suspend fun createFavoriteSet(
        name: String,
        cover: String
    ): Boolean {
        return api.addFavoritesSet(CreateFavoritesSetRequest(name, cover))
            .handleResponse("创建收藏夹失败").let { it == null }
    }

    suspend fun addToFavorite(
        favoritesSetId: Int,
        articleId: String
    ): Boolean {
        return api.addFavorites(
            AddFavoritesRequest(
                favorite_set = favoritesSetId,
                article = articleId,
                method = "add"
            )
        ).handleResponse("新建收藏夹失败").let { it == null }
    }

    suspend fun deleteFavorite(
        favoritesSetId: Int,
        articleId: String
    ): Boolean {
        return api.addFavorites(
            AddFavoritesRequest(
            favorite_set = favoritesSetId,
            article = articleId,
            method = "remove"
            )
        ).handleResponse("删除收藏失败").let { it == null }
    }

    suspend fun checkIfFavorite(
        id: Int,
        article: String
    ): Boolean {
        return api.checkFavorite(id, article).handleResponse("检查收藏状态失败") as Boolean
    }

    suspend fun deleteFavoriteSetById(
        id: Int
    ): Boolean {
        return api.deleteFavorites(id)
            .handleResponse("删除收藏夹失败").let { it == null }
    }



}