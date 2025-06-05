package site.smartenglish.remote.data

typealias SearchWordResponse = List<SearchWordResponseElement>

data class GetWordResponse(
    val word: String? = null,
    val phonetic: String? = null,
    val pronunciation: String? = null,
    val explanations: List<String>? = null,
    val synonyms: SynonymsOrAntonyms? = null,
    val antonyms: SynonymsOrAntonyms? = null,
    val examples: List<Example?>? = null
)

data class SearchWordResponseElement(
    val word: String? = null,
    val explanations: List<String?>? = null
)

