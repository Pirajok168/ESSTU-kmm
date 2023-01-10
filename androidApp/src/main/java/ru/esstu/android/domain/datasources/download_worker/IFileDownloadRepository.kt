package ru.esstu.android.domain.datasources.download_worker

import ru.esstu.student.messaging.entities.MessageAttachment

interface IFileDownloadRepository {
    fun downloadFile(attachment: MessageAttachment)
}