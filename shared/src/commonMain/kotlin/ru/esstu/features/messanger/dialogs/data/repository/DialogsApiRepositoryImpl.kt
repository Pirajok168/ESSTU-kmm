package ru.esstu.features.messanger.dialogs.data.repository


import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.dialogs.data.api.DialogsApi
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog
import ru.esstu.features.messanger.dialogs.domain.toDialogs


class DialogsApiRepositoryImpl(
    private val dialogsApi: DialogsApi,
) : IDialogsApiRepository {
    override suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>> =
        dialogsApi.getDialogs(offset, limit).transform { it.toDialogs() }
}