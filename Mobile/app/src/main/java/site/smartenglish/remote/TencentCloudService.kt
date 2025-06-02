package site.smartenglish.remote

import android.content.Context
import android.net.Uri
import android.util.Log
import com.tencent.cos.xml.CosXmlService
import com.tencent.cos.xml.CosXmlServiceConfig
import com.tencent.cos.xml.exception.CosXmlClientException
import com.tencent.cos.xml.exception.CosXmlServiceException
import com.tencent.cos.xml.listener.CosXmlResultListener
import com.tencent.cos.xml.model.CosXmlRequest
import com.tencent.cos.xml.model.CosXmlResult
import com.tencent.cos.xml.transfer.COSXMLUploadTask
import com.tencent.cos.xml.transfer.TransferConfig
import com.tencent.cos.xml.transfer.TransferManager
import com.tencent.cos.xml.transfer.TransferState
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import site.smartenglish.BuildConfig
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class TencentCloudService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // 腾讯云配置 - 替换为您的实际信息
    private val region = "ap-beijing" // 存储桶地域
    private val bucketName = "ofindex-1318773774" // 存储桶名称
    private val secretId = BuildConfig.SecretId // API密钥ID
    private val secretKey = BuildConfig.SecretKey // API密钥Key

    private val transferManager: TransferManager by lazy {
        val serviceConfig = CosXmlServiceConfig.Builder()
            .setRegion(region)
            .isHttps(true)
            .builder()

        // 使用永久密钥初始化
        val credentialProvider = ShortTimeCredentialProvider(secretId, secretKey, 300);

        val cosXmlService = CosXmlService(context, serviceConfig, credentialProvider)

        // 上传配置
        val transferConfig = TransferConfig.Builder().build()

        TransferManager(cosXmlService, transferConfig)
    }

    /**
     * 上传图片到腾讯云 COS
     */
    suspend fun uploadImage(localUri: Uri): String {
        // 1. 从 Uri 获取实际文件路径
        val filePath = getFilePathFromUri(localUri)
            ?: throw Exception("无法解析文件路径")

        // 2. 生成唯一的云端文件名
        val timestamp = System.currentTimeMillis()
        val remoteFileName = "avatar/${timestamp}_${File(filePath).name}"

        // 3. 使用协程方式处理上传
        return suspendCancellableCoroutine { continuation ->
            // 创建上传任务
            val uploadTask = transferManager.upload(
                bucketName,
                remoteFileName,
                filePath,
                null
            )

            // 设置回调
            uploadTask.setCosXmlResultListener(object : CosXmlResultListener {
                override fun onSuccess(request: CosXmlRequest, result: CosXmlResult) {
                    val imageUrl = "https://${bucketName}.cos.${region}.myqcloud.com/$remoteFileName"
                    Log.d("TencentCloud", "上传成功: $imageUrl")
                    continuation.resume(imageUrl)
                }

                override fun onFail(
                    request: CosXmlRequest,
                    clientException: CosXmlClientException?,
                    serviceException: CosXmlServiceException?
                ) {
                    val error = clientException?.message ?: serviceException?.message ?: "未知上传错误"
                    Log.e("TencentCloud", "上传失败: $error")
                    continuation.resumeWithException(Exception("上传失败: $error"))
                }
            })

            uploadTask.setTransferStateListener { state ->
                if (state == TransferState.FAILED) {
                    Log.e("TencentCloud", "上传失败: TransferState.FAILED")
                    continuation.resumeWithException(Exception("上传失败，请重试"))
                }
            }
        }
    }

    // 获取文件路径
    private fun getFilePathFromUri(uri: Uri): String? {
        return when (uri.scheme) {
            "file" -> uri.path
            "content" -> {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val tempFile = File.createTempFile("upload_", ".tmp", context.cacheDir)
                    tempFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    tempFile.absolutePath
                }
            }
            else -> null
        }
    }
}