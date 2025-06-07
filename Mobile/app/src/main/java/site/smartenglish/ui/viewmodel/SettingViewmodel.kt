package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import site.smartenglish.manager.DataStoreManager
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _targetWordCount = MutableStateFlow(10)
    val targetWordCount: StateFlow<Int> = _targetWordCount

    init {
        // 在初始化时从DataStoreManager加载保存的值
        loadSavedWordCount()
    }

    fun loadSavedWordCount() {
        // 从DataStoreManager中获取当前保存的值
        val savedCount = dataStoreManager.getLearnWordsCountSync()
        _targetWordCount.value = savedCount
    }

    fun updateTargetWordCount(count: Int) {
        viewModelScope.launch {
            dataStoreManager.saveLearnWordsCount(count)
            _targetWordCount.value = count
        }
    }
}