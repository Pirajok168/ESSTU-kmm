package ru.esstu.features.messanger.dialogs.data.repository

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog

interface IDialogsApiRepository {
    suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>>
}