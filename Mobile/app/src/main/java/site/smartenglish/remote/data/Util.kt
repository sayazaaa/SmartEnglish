package site.smartenglish.remote.data

data class WordBook(
    val id: Int? = null,
    val name: String? = null,
    val cover: String? = null,
    val wordcount: Int? = null,
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