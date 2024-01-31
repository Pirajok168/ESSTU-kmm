package ru.esstu.features.messanger.dialogs.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog

interface DialogsInteractor {

    suspend fun getLocalDialogs(limit: Int, offset: Int): List<PreviewDialog>

    suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>>

    suspend fun clearDialogs()
}