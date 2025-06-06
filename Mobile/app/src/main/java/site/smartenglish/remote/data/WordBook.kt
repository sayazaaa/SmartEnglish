package site.smartenglish.remote.data

typealias GetWordBookInfoResponse = List<String?>?

typealias GetWordBookListResponse = List<GetWordBookListResponseElement?>?

typealias  Get20NewWordResponse = List<String?>?

data class GetWordBookListResponseElement(
    val id: Int? = null,
    val name: String? = null,
    val cover: String? = null,
    val wordcount: Int? = null
)
