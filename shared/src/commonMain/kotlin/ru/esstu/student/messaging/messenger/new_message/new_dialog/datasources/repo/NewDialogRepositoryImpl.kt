package ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo

import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.toToken
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors
import ru.esstu.student.messaging.dialog_chat.datasources.toMessage
import ru.esstu.student.messaging.dialog_chat.datasources.toReplyMessage
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toPreviewLastMessage
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApi

class NewDialogRepositoryImpl(
    private val api: NewDialogApi,
    private val dialogCacheDao: CacheDao,
    private val loginDataRepository: ILoginDataRepository,
): INewDialogRepository {
    override suspend fun findUsers(query: String, limit: Int, offset: Int): Response<List<Sender>> =
        api.findUsers(query = query, limit = limit, offset = offset).transform { it.mapNotNull { it.toUser() } }

    override suspend fun sendMessage(
        dialogId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<Long> {
        if (message.isNotBlank()  && attachments.any()) {
            val result = api.sendAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message,
                    IPeer_.DialoguePeer(dialogId),
                )
            )

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if (attachments.any()) {
            val result =  api.sendMessageWithAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message,
                    IPeer_.DialoguePeer(dialogId),
                )
            )
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message.isNotBlank()) && attachments.isEmpty()) {
            val result = api.sendMessage(
                body = ChatMessageRequestBody(
                    message,
                    IPeer_.DialoguePeer(dialogId),
                )
            )
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        return Response.Error(ResponseError(message = "Неизвестное состояние"))
    }

    // TODO: Потесить быть её 
    override suspend fun updateDialogOnPreview(opponent: Sender, messageId: Long): Response<Unit> {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return Response.Error(
            ResponseError(error = ServerErrors.Unauthorized)
        )
        val messageResponse = api
            .pickMessages(messageId.toString())
            .data
            ?.firstOrNull()

        val replyResponse = if (messageResponse?.replyToMsgId != null)
            api.pickMessages(messageResponse.replyToMsgId.toString()).data?.firstOrNull()
        else
            null

        val authors = api.pickUsers(
            usersIds = listOfNotNull(replyResponse?.from, messageResponse?.from)
                .joinToString()
        ).transform { it.mapNotNull { it.toUser() } }

        val replyMessage = replyResponse?.toReplyMessage(authors.data.orEmpty())

        val message = messageResponse?.toMessage(
            replyMessages = listOfNotNull(replyMessage),
            authors = authors.data.orEmpty()
        )

        val dialog = PreviewDialog(
            id = opponent.id,
            opponent,
            lastMessage = message?.toPreviewLastMessage(),
            notifyAboutIt = true,
            unreadMessageCount = 0
        )
        dialogCacheDao.updateDialogLastMessage(
            appUserId,
            dialogId = opponent.id,
            lastMessage = message?.toPreviewLastMessage()!!,
            dialog
        )
        return Response.Success(Unit)
    }
}