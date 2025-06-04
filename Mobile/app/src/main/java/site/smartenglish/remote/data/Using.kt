package site.smartenglish.remote.data

data class PutUsingRequest(
    val function: String? = null,
    val duration: Int
)

typealias GetUsingResponse = Int