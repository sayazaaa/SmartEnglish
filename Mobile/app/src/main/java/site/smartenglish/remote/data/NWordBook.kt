package site.smartenglish.remote.data

typealias GetNWordBookWordListResponse = List<String?>?

typealias GetNWordBookListResponse = List<GetNWordBookListResponseElement?>?

data class GetNWordBookListResponseElement(
    val id: Int? = null,
    val name: String? = null,
    val cover: String? = null
)

data class CreateNWordBookRequest(
    val name: String,
    val cover: String
)

data class AddNWordBookWordRequest(
    val word: String,
    val nwordbook: Int,
    val method:String
)