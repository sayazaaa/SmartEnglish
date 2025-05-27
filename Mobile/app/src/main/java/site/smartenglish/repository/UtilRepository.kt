package site.smartenglish.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.toBitmap
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

@Singleton
class UtilRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun loadImageAsBitmap(imageUrl: String): Result<Bitmap> {
        return suspendCancellableCoroutine { continuation ->
            try {
                Log.d("UtilRepository", "Loading image from URL: $imageUrl")
                val loader = ImageLoader.Builder(context).build()

                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .listener(
                        onError = { _, throwable ->
                            Log.e("UtilRepository", "图片加载失败", throwable.throwable)
                            if (continuation.isActive) {
                                continuation.resume(Result.failure(throwable.throwable))
                            }
                        },
                        onSuccess = { _, result ->
                            Log.d("UtilRepository", "图片加载成功")
                            if (continuation.isActive) {
                                val bitmap = result.image.toBitmap()
                                Log.d(
                                    "UtilRepository",
                                    "图片尺寸: ${bitmap.width}x${bitmap.height}"
                                )
                                continuation.resume(Result.success(bitmap))
                            } else {
                                Log.e("UtilRepository", "无法获取图片Bitmap")
                                continuation.resume(Result.failure(Exception("无法获取图片Bitmap")))
                            }
                        }
                    )
                    .build()

                loader.enqueue(request)
            } catch (e: Exception) {
                Log.e("UtilRepository", "加载图片过程中发生错误", e)
                if (continuation.isActive) {
                    continuation.resume(Result.failure(e))
                }
            }
        }
    }
}