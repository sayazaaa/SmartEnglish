package site.smartenglish.remote.data

data class ChangeProfileRequest(
    val name: String,
    val description: String,
    val avatar: String? = null
)

data class ChangeProfileResponse(
    val name: String,
    val description: String,
    val avatar: String? = null,
    val wordbook: WordBook
)

