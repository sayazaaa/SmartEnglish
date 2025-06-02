package site.smartenglish.remote.data

data class ChangeProfileRequest(
    val name: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val wordbookid: Int? = null,
    val new_word_count: Int? = null,
    val today_review: Int? = null
)

data class ChangeProfileResponse(
    val name: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val wordbook: WordBook? = null
)

data class GetProfileResponse(
    val name: String? = null,
    val description: String? = null,
    val avatar: String? = null,
    val wordbook: WordBook? = null,
    val new_word_count: Int? = null,
    val today_review: Int? = null
)

