package ru.esstu.student.messaging.messenger.new_message.new_dialog.domain.repo

import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Sender

interface INewDialogRepository {
    suspend fun findUsers(query: String, limit: Int, offset: Int): Response<List<Sender>>

    suspend fun sendMessage(
        dialogId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<Long>

    suspend fun updateDialogOnPreview(opponent: Sender, messageId: Long): Response<Unit>
}