package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import site.smartenglish.remote.data.GetWordResponse
import site.smartenglish.repository.WordRepository
import javax.inject.Inject

@HiltViewModel
class WordDetailViewmodel @Inject constructor(
    private val wordRepository: WordRepository,
) : ViewModel() {
    private var currentWordIndex = 0

    private val _wordDetail = MutableStateFlow(GetWordResponse())
    val wordDetail = _wordDetail.asStateFlow()

    private val _snackBar = MutableStateFlow("")
    val snackBar = _snackBar.asStateFlow()

    fun getWordDetail(word: String) {
        viewModelScope.launch {
            try {
                val wordInfo = wordRepository.getWordInfo(word)
                _wordDetail.value = wordInfo

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearSnackBar() {
        _snackBar.value = ""
    }


}