package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog

interface IDialogsApiRepository {
    suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>>
}