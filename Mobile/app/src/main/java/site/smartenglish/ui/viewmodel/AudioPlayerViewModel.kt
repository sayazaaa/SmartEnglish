package site.smartenglish.ui.viewmodel

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AudioPlayerViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun playAudio(audioUrl: String?) {
        if (audioUrl.isNullOrEmpty()) {
            Log.e("AudioPlayerViewModel", "音频URL为空")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 停止当前正在播放的音频
                stopAudio()

                // 创建新的MediaPlayer
                mediaPlayer = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build()
                    )
                    setDataSource(audioUrl)
                    setOnPreparedListener {
                        start()
                        _isPlaying.value = true
                    }
                    setOnCompletionListener {
                        _isPlaying.value = false
                    }
                    setOnErrorListener { _, _, _ ->
                        _isPlaying.value = false
                        true
                    }
                    prepareAsync()
                }
            } catch (e: Exception) {
                Log.e("AudioPlayerViewModel", "播放音频失败", e)
                _isPlaying.value = false
            }
        }
    }

    fun stopAudio() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
            mediaPlayer = null
            _isPlaying.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }
}