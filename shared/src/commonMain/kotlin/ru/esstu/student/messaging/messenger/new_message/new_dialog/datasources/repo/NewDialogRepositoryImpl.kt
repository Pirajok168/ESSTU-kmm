package ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.dialog_chat.datasources.toMessage
import ru.esstu.student.messaging.dialog_chat.datasources.toReplyMessage
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDao
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.new_message.new_dialog.datasources.api.NewDialogApi

class NewDialogRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: NewDialogApi,
    private val dialogCacheDao: CacheDao
): INewDialogRepository {
    override suspend fun findUsers(query: String, limit: Int, offset: Int): Response<List<Sender>> {
        return auth.provideToken { type, token ->
            api.findUsers("$type $token", query = query, offset = offset, limit = limit).mapNotNull { it.toUser() }
        }
    }

    override suspend fun sendMessage(
        dialogId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<Long> {
        if (message.isNotBlank()  && attachments.any()) {
            val result = auth.provideToken { type, token ->
                api.sendAttachments(
                    authToken = "$token",
                    files = attachments,
                    requestSendMessage = ChatMessageRequestBody(
                        message,
                        IPeer_.DialoguePeer(dialogId),
                    )
                )
            }

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if (attachments.any()) {
            val result = auth.provideToken { type, token ->
                api.sendMessageWithAttachments(
                    authToken = "$token",
                    files = attachments,
                    requestSendMessage = ChatMessageRequestBody(
                        message,
                        IPeer_.DialoguePeer(dialogId),
                    )
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message.isNotBlank()) && attachments.isEmpty()) {
            val result = auth.provideToken { type, token ->
                api.sendMessage(
                    authToken = "$token",
                    body = ChatMessageRequestBody(
                        message,
                        IPeer_.DialoguePeer(dialogId),
                    )
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        return Response.Error(ResponseError(message = "Неизвестное состояние"))
    }

    override suspend fun updateDialogOnPreview(opponent: Sender, messageId: Long): Response<Unit> {
        return auth.provideToken {
                token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported user")

            val messageResponse = api
                .pickMessages(token.access, messageId.toString())
                .firstOrNull()

            val replyResponse = if (messageResponse?.replyToMsgId != null)
                api.pickMessages(token.access, messageResponse.replyToMsgId.toString()).firstOrNull()
            else
                null

            val authors = api.pickUsers(
                authToken = "${token.type} ${token.access}",
                usersIds = listOfNotNull(replyResponse?.from, messageResponse?.from)
                    .joinToString()
            ).mapNotNull { it.toUser() }

            val replyMessage = replyResponse?.toReplyMessage(authors)

            val message = messageResponse?.toMessage(
                replyMessages = listOfNotNull(replyMessage),
                authors = authors
            )


        }
    }
}