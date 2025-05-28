package site.smartenglish.remote.data

data class GetWordResponse(
    val word: String? = null,
    val phonetic: String? = null,
    val pronunciation: String? = null,
    val explanations: List<String>? = null,
    val synonyms: SynonymsOrAntonyms? = null,
    val antonyms: SynonymsOrAntonyms? = null,
    val examples: Example? = null
)

data class SearchWordResponse(
    val word: String? = null,
    val explanations: List<String?>? = null
)

