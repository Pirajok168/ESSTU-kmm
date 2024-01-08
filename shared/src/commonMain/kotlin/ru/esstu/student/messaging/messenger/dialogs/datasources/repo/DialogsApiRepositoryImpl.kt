package ru.esstu.student.messaging.messenger.dialogs.datasources.repo


import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.messenger.dialogs.datasources.api.DialogsApi
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog


class DialogsApiRepositoryImpl constructor(
    private val dialogsApi: DialogsApi,
) : IDialogsApiRepository {
    override suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>> =
        dialogsApi.getDialogs(offset, limit).transform { it.toDialogs() }
}