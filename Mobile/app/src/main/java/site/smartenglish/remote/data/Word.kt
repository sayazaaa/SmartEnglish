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

data class SynonymsOrAntonyms(
    val a: List<String>? = null,
    val v: List<String>? = null,
    val n: List<String>? = null,
)

data class Example(
    val english: String? = null,
    val chinese: String? = null,
    val audio: String? = null
)