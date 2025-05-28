package site.smartenglish.remote.data

typealias GetFavoritesSetListResponse = List<GetFavoritesSetListResponseElement?>?
typealias GetFavoritesListResponse = List<GetFavoritesListResponseElement?>?

data class GetFavoritesSetListResponseElement(
    val id: Int? = null,
    val name: String? = null,
    val cover: String? = null
)

data class GetFavoritesListResponseElement(
    val id: Int? = null,
    val title: String? = null,
    val cover: String? = null,
    val date: Int? = null,
    val tags: List<String?>? = null
)

data class CreateFavoritesSetRequest(
    val name: String,
    val cover: String
)

data class AddFavoritesRequest(
    val favorite_set: Int? = null,
    val article: Int? = null,
)

data class DeleteFavoritesRequest(
    val id: Int? = null,
)