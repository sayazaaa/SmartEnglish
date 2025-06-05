package site.smartenglish.remote.data

typealias GetWordSetResponse = List<GetWordSetResponseElement?>?

typealias PutWordSetRequest = List<PutWordSetRequestElement?>?

data class GetWordSetResponseElement(
    val word: String? = null,
    val stage: Int? = null
)

data class PutWordSetRequestElement(
    val word: String? = null,
    val stage: Int? = null
)

