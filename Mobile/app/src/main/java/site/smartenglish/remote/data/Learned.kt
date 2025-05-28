package site.smartenglish.remote.data

typealias GetLearnedResponse = List<String?>?

data class PutLearnedRequest(
    val word: String? = null,
    val review_date: String? = null,
    val status: String? = null
)

data class PutLearnedResponse(
    val new_word: String? = null,
    val message: String? = null
)