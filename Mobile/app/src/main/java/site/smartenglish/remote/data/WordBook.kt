package site.smartenglish.remote.data

typealias GetWordBookInfoResponse = List<String?>?

typealias GetWordBookListResponse = List<GetWordBookListResponseElement?>?

data class GetWordBookListResponseElement(
    val id: Int? = null,
    val name: String? = null,
    val cover: String? = null,
    val wordcount: Int? = null
)
