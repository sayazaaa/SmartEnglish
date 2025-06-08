package site.smartenglish.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import site.smartenglish.repository.UsingRepository
import javax.inject.Inject

@HiltViewModel
class UsingViewModel @Inject constructor(
    private val usingRepository: UsingRepository
) : ViewModel() {

    private var startTime: Long = 0
    private var totalDuration: Int = 0

    // 开始计时
    fun startTracking() {
        startTime = System.currentTimeMillis()
    }

    // 暂停计时并累计时长
    fun pauseTracking() {
        if (startTime > 0) {
            val duration = (System.currentTimeMillis() - startTime) / 1000
            totalDuration += duration.toInt()
            startTime = 0
        }
    }

    // 发送使用时长数据到服务器
    // "learn"/"review"/"listen"/"read"
    fun sendUsageTime(name:String) {
        // 如果还在计时中，先暂停累计
        pauseTracking()

        if (totalDuration > 0) {
            viewModelScope.launch {
                try {
                    usingRepository.updateUsingTime(name,totalDuration/60)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // 重置计时器
                    totalDuration = 0
                }
            }
        }
    }
}