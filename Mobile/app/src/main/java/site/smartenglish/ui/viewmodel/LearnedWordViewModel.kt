package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetLearnedResponseElement
import site.smartenglish.repository.LearnedRepository
import javax.inject.Inject

@HiltViewModel
class LearnedWordViewModel @Inject constructor(
    private val learnedRepository: LearnedRepository
) : ViewModel() {

    private val _learnedWords = MutableStateFlow<List<Pair<String,String>>>(emptyList())
    val learnedWords = _learnedWords.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun getLearnedWords() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val words = learnedRepository.getLearnedWordList()
                _learnedWords.value = words?.filterNotNull()?.map { item ->
                    Pair(
                        item.word ?: "",
                        item.review_date ?: ""
                    )
                } ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}