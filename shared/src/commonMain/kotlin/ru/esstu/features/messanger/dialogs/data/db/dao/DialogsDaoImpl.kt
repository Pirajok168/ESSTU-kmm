package ru.esstu.features.messanger.dialogs.data.db.dao

import ru.esstu.features.messanger.dialogs.data.db.DialogQueries
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog
import ru.esstu.features.messanger.dialogs.domain.toUser
import ru.esstu.features.messanger.dialogs.domain.toUserEntity

class DialogsDaoImpl(
    val dbQueries: DialogQueries
) : DialogsDao {
    override suspend fun setDialog(previewDialog: PreviewDialog) =
        dbQueries.setDialog(
            idDialog = previewDialog.id,
            lastMessage = previewDialog.lastMessage,
            opponent = previewDialog.opponent.toUserEntity(),
            unread = previewDialog.unreadMessageCount,
            notifyAboutIt = previewDialog.notifyAboutIt
        )

    override suspend fun getDialogs(pageSize: Int, pageOffset: Int): List<PreviewDialog> =
        dbQueries.getDialogs(pageSize.toLong(), pageOffset.toLong()).executeAsList().map {
            PreviewDialog(
                it.idDialog,
                it.opponent.toUser(),
                it.lastMessage,
                it.notifyAboutIt,
                it.unread
            )
        }

    override suspend fun clearDialogs() =
        dbQueries.clear()

}