package ru.esstu.android.domain.datasources.download_worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.esstu.android.R
import ru.esstu.android.domain.datasources.download_worker.entities.LoadingFile
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import kotlin.math.roundToInt

enum class NotificationTypes {
    NOTIFICATION_WITH_PROGRESS,
    NOTIFICATION_WITHOUT_PROGRESS,
    NO_NOTIFICATION
}


private class Notification(context: Context, private val notificationType: NotificationTypes, channel: String, private val file: LoadingFile) {

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Загрузка файлов"
            val descriptionText = "Канал для отображение загрузки файлов"
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(channel, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)

            notificationManager?.createNotificationChannel(channel)
        }
    }

    private val notification: NotificationCompat.Builder = NotificationCompat.Builder(context, channel)
        .setSmallIcon(R.drawable.ic_notification_logo)
        .setContentTitle("Выполняется загрузка файла")
        .setContentText("${file.name}.${file.ext}")
        .setPriority(NotificationCompat.PRIORITY_LOW)
        .setOngoing(true)

    private var notificationIsShown = false

    fun show(context: Context, progress: Float? = null) {
        when (notificationType) {

            NotificationTypes.NOTIFICATION_WITH_PROGRESS -> {
                progress?.run {
                    notification.setProgress(100, (progress * 100).roundToInt(), false)
                }
                NotificationManagerCompat.from(context).notify(file.id, notification.build())
            }

            NotificationTypes.NOTIFICATION_WITHOUT_PROGRESS ->
                if (!notificationIsShown) NotificationManagerCompat.from(context).notify(file.id, notification.build())

            NotificationTypes.NO_NOTIFICATION -> return
        }
        notificationIsShown = true
    }

    fun cancel(context: Context) =
        NotificationManagerCompat.from(context).cancel(file.id)

}
class FileDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    companion object{
        val fileParams get() = FileParams
        val WORKER_ID: String = FileDownloadWorker::class.java.simpleName
        object FileParams {
            val KEY_FILE_ID: String get() = FileParams::KEY_FILE_ID.name
            val KEY_FILE_TYPE: String get() = FileParams::KEY_FILE_TYPE.name
            val KEY_FILE_NAME: String get() = FileParams::KEY_FILE_NAME.name
            val KEY_FILE_SIZE: String get() = FileParams::KEY_FILE_SIZE.name
            val KEY_FILE_EXT: String get() = FileParams::KEY_FILE_EXT.name
            val KEY_FILE_URI: String get() = FileParams::KEY_FILE_URI.name
        }

        val responseParams get() = ResponseParams

        object ResponseParams {
            val KEY_PROGRESS_VAL: String get() = ResponseParams::KEY_PROGRESS_VAL.name
            val KEY_FILE_URI: String get() = ResponseParams::KEY_FILE_URI.name
            val KEY_ERROR_MESSAGE: String get() = ResponseParams::KEY_ERROR_MESSAGE.name
        }
    }


    override suspend fun doWork(): Result = withContext(Dispatchers.IO){
        val file = inputData.toLoadingFile() ?: return@withContext Result.failure()
        val notification = Notification(
            channel = WORKER_ID,
            notificationType =  NotificationTypes.NOTIFICATION_WITH_PROGRESS,
            context = applicationContext,
            file = file
        )
        val result = downloadFile(file){
            isLoading, progress ->
            if (isLoading){
                setProgress(workDataOf(ResponseParams.KEY_PROGRESS_VAL to progress))
                notification.show(applicationContext, progress)
            }else{
                notification.cancel(applicationContext)
            }
        }

        return@withContext when(result){
            is Response.Error -> Result.failure(workDataOf(ResponseParams.KEY_ERROR_MESSAGE to result.error.message.orEmpty()))
            is Response.Success -> Result.success(workDataOf(ResponseParams.KEY_FILE_URI to result.data.toString()))
        }
    }


    private suspend fun downloadFile(
        file: LoadingFile,
        onLoading: suspend (isLoading: Boolean, progress: Float) -> Unit
    ): Response<Uri> {
        onLoading(true, 0f)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "${file.name}.${file.ext}")
                put(MediaStore.MediaColumns.MIME_TYPE, file.type)

                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "Download"
                )
            }
            val resolver = applicationContext.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            if (uri != null) {
                val result = kotlin.runCatching {
                    URL(file.uri).openStream()
                        .use { input ->
                            resolver.openOutputStream(uri)?.use { output ->

                                var bytesCopied: Long = 0
                                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                                var bytes = input.read(buffer)

                                while (bytes >= 0) {
                                    output.write(buffer, 0, bytes)
                                    bytesCopied += bytes
                                    bytes = input.read(buffer)

                                    onLoading(true, bytesCopied / file.size.toFloat())
                                }

                            }

                        }
                }
                onLoading(false, 1f)
                return if (result.isSuccess) {
                    Response.Success(uri)
                } else
                    Response.Error(ResponseError(message = result.exceptionOrNull()?.message))
            }else{
                onLoading(false, 1f)
                return Response.Error(ResponseError(message = "resolver uri is empty"))
            }
        }else{
            val target = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "${file.name}.${file.ext}"
            )

            val result = kotlin.runCatching {

                URL(file.uri).openStream().use { input ->
                    FileOutputStream(target).use { output ->

                        var bytesCopied: Long = 0
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var bytes = input.read(buffer)


                        while (bytes >= 0) {
                            output.write(buffer, 0, bytes)
                            bytesCopied += bytes
                            bytes = input.read(buffer)

                            onLoading(true, bytesCopied / file.size.toFloat())
                        }

                    }
                }
            }

            onLoading(false, 1f)

            return if (result.isSuccess)
                Response.Success(target.toUri())
            else
                Response.Error(ResponseError(message = result.exceptionOrNull()?.message))
        }
    }
}