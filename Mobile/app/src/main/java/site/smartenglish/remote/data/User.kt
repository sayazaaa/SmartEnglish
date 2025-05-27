package site.smartenglish.remote.data

data class ChangeProfileRequest(
    val name: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val wordbookid: Int? = null
)

data class ChangeProfileResponse(
    val name: String,
    val description: String,
    val avatar: String,
    val wordbook: WordBook
)

data class GetProfileResponse(
    val name: String,
    val description: String,
    val avatar: String,
    val wordbook: WordBook
)

