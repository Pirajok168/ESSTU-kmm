package ru.esstu.android.domain.datasources.download_worker

import androidx.work.Data
import ru.esstu.android.domain.datasources.download_worker.entities.LoadingFile
import ru.esstu.student.messaging.entities.MessageAttachment

fun MessageAttachment.toWorkData() = Data
    .Builder()
    .apply {
        putString(FileDownloadWorker.fileParams.KEY_FILE_TYPE, type)
        putLong(FileDownloadWorker.fileParams.KEY_FILE_SIZE, size.toLong())
        putString(FileDownloadWorker.fileParams.KEY_FILE_NAME, name)
        putString(FileDownloadWorker.fileParams.KEY_FILE_EXT, ext)
        putString(FileDownloadWorker.fileParams.KEY_FILE_URI, fileUri)
        putInt(FileDownloadWorker.fileParams.KEY_FILE_ID, id)
    }
    .build()


fun Data.toLoadingFile(): LoadingFile? {
    return LoadingFile(
        id = getInt(FileDownloadWorker.Companion.FileParams.KEY_FILE_ID, 0),
        uri = getString(FileDownloadWorker.Companion.FileParams.KEY_FILE_URI) ?: return null,
        ext = getString(FileDownloadWorker.Companion.FileParams.KEY_FILE_EXT) ?: return null,
        name = getString(FileDownloadWorker.Companion.FileParams.KEY_FILE_NAME) ?: return null,
        type = getString(FileDownloadWorker.Companion.FileParams.KEY_FILE_TYPE) ?: return null,
        size = getLong(FileDownloadWorker.Companion.FileParams.KEY_FILE_SIZE, 0)
    )
}