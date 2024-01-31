package ru.esstu.features.messanger.dialogs.data.db.dao

import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog

interface DialogsDao {
    suspend fun setDialog(previewDialog: PreviewDialog)

    suspend fun getDialogs(pageSize: Int, pageOffset: Int): List<PreviewDialog>

    suspend fun clearDialogs()
}