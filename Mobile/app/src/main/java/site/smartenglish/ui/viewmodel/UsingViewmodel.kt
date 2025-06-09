package site.smartenglish.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import site.smartenglish.di.ApplicationScope
import site.smartenglish.repository.UsingRepository
import javax.inject.Inject

@HiltViewModel
class UsingViewModel @Inject constructor(
    private val usingRepository: UsingRepository,
    @ApplicationScope private val appScope: CoroutineScope
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
    fun sendUsageTime(name: String) {
        pauseTracking()

        val duration = totalDuration
        if (duration <= 0) return

        appScope.launch {
            try {
                usingRepository.updateUsingTime(name, duration / 60)
                Log.d("UsingVM", "使用时长已累计: $duration 秒")
            } catch (e: Exception) {
                Log.e("UsingVM", "上报失败", e)
            } finally {
                totalDuration = 0
            }
        }
    }
}