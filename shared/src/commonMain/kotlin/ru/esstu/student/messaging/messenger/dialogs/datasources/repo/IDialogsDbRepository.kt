package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog

interface IDialogsDbRepository {
    suspend fun getDialogs(limit: Int, offset: Int): List<PreviewDialog>

    suspend fun clear()

    suspend fun setDialogs(previewDialogs: List<PreviewDialog>)

    suspend fun deleteDialog(id: String)
}