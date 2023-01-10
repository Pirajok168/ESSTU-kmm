package ru.esstu.android.domain.datasources.download_worker

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.esstu.android.domain.datasources.download_worker.entities.LoadingFile
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import java.net.URL

class FileDownloadWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(appContext, params) {
    companion object{
        val fileParams get() = FileParams

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

        val result = downloadFile(file){
            isLoading, progress ->
            if (isLoading){
                setProgress(workDataOf(ResponseParams.KEY_PROGRESS_VAL to progress))
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
            return Response.Error(ResponseError(message = "Не реализовано"))
        }
    }
}