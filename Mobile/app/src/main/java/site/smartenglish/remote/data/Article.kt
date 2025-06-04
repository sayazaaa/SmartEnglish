package site.smartenglish.remote.data

typealias SearchArticleResponse = List<SearchArticleResponseElement?>?

data class GetArticleResponse(
    val content: String? = null,
    val cover: String? = null,
    val date: String? = null,
    val id: String? = null,
    val tags: List<String?>? = null,
    val title: String? = null
)

data class SearchArticleResponseElement(
    val cover: String? = null,
    val date: String? = null,
    val id: String? = null,
    val tags: List<String?>? = null,
    val title: String? = null
)