package site.smartenglish.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.repository.WordRepository
import site.smartenglish.ui.compose.WordSearchItemData
import javax.inject.Inject

@HiltViewModel
class HomeViewmodel @Inject constructor(
    private val wordRepository: WordRepository
) : ViewModel() {
    private val _searchResult = MutableStateFlow<List<WordSearchItemData>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    fun searchWord(word: String) {
        viewModelScope.launch {
            try {
                val response = wordRepository.searchWord(word)
                val items = response.mapNotNull {
                    if (it.word.isNullOrBlank() || it.explanations.isNullOrEmpty())
                        return@mapNotNull null
                    WordSearchItemData(
                        title = it.word,
                        description = it.explanations.joinToString(separator = " "),
                    )
                }
                _searchResult.value = items
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearSearchResult() {
        _searchResult.value = emptyList()
    }
}