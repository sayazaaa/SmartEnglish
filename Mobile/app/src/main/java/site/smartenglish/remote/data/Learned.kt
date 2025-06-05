package site.smartenglish.remote.data

typealias GetLearnedResponse = List<GetLearnedResponseElement>?

data class PutLearnedRequest(
    val word: String? = null,
    val review_date: String? = null,
    val status: String? = null
)

data class PutLearnedResponse(
    val new_word: String? = null,
    val message: String? = null
)

data class GetLearnedResponseElement(
    val word: String? = null,
    val review_date: String? = null
)