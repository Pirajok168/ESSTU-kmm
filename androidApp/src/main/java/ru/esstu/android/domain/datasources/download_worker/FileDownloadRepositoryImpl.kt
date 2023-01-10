package ru.esstu.android.domain.datasources.download_worker

import android.content.Context
import androidx.work.*
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.esstu.student.messaging.entities.MessageAttachment
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FileDownloadRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): IFileDownloadRepository {
    private val workManager = WorkManager.getInstance(context)

    override fun downloadFile(attachment: MessageAttachment) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val fileDownloadRequest = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setConstraints(constraints)
            .setInputData(attachment.toWorkData())
            .build()

        workManager.enqueueUniqueWork(
            attachment.id.toString(),
            ExistingWorkPolicy.KEEP,
            fileDownloadRequest
        )
    }
}