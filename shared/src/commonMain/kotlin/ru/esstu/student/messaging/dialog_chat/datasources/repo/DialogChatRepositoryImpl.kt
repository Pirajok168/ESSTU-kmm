package ru.esstu.student.messaging.dialog_chat.datasources.repo


import io.ktor.client.request.forms.*
import io.ktor.util.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.toToken
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatReadRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors
import ru.esstu.student.messaging.dialog_chat.datasources.*
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.HistoryCacheDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.OpponentDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.toEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.toErredMessageEntity
import ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages.toSentUserMessage
import ru.esstu.student.messaging.dialog_chat.datasources.db.user_messages.UserMessageDao
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.NewUserMessage
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.entities.SentUserMessage
import ru.esstu.student.messaging.messenger.datasources.toUser
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toPreviewLastMessage


class DialogChatRepositoryImpl constructor(
    private val dialogChatApi: DialogChatApi,
    private val cacheDao: HistoryCacheDao,
    private val opponentDao: OpponentDao,
    private val userMsgDao: UserMessageDao,
    private val erredMsgDao: ErredMessageDao,
    private val historyCacheDaoNew: CacheDao,
    private val loginDataRepository: ILoginDataRepository,
) : IDialogChatRepository {


    override suspend fun getOpponent(id: String): Flow<FlowResponse<Sender>> = flow {
        emit(FlowResponse.Loading())

        val cachedOpponent = opponentDao.getOpponent(id)?.toUser()
        if (cachedOpponent != null)
            emit(FlowResponse.Success(cachedOpponent))

        when (val response = dialogChatApi.getOpponent(id)) {
            is Response.Error -> emit(FlowResponse.Error(response.error))
            is Response.Success -> {
                val remoteOpponent = response.data.user.toUser()
                if (remoteOpponent == null)
                    emit(FlowResponse.Error(ResponseError(message = "Cast Exception")))
                else {
                    opponentDao.insert(remoteOpponent.toUserEntityOpponent())
                    if (remoteOpponent != cachedOpponent)
                        emit(FlowResponse.Success(remoteOpponent))
                }
            }
        }
        emit(FlowResponse.Loading(false))
    }


    override suspend fun getPage(
        dialogId: String,
        limit: Int,
        offset: Int
    ): Response<List<Message>> {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return Response.Error(error = ResponseError(error = ServerErrors.Unauthorized))
        val cached = cacheDao.getMessageHistory(
            limit = limit,
            offset = offset,
            opponentId = dialogId,
            appUserId =appUserId
        ).map { it.toMessage() }

        if (cached.isNotEmpty())
            return Response.Success(cached)

        val remotePage = dialogChatApi.getHistory(dialogId, offset, limit)
            .transform {
                it.toMessages(
                    provideReplies = { indices ->
                        dialogChatApi.pickMessages(indices.joinToString()).data.orEmpty()
                    },
                    provideUsers = { indices ->
                        dialogChatApi.pickUsers(indices.joinToString()).data.orEmpty()
                    }
                )
            }


        return when (remotePage) {
            is Response.Error -> Response.Error(remotePage.error)
            is Response.Success -> {
                setMessages(dialogId, remotePage.data)
                Response.Success(remotePage.data)
            }
        }
    }

    override suspend fun setMessages(dialogId: String, messages: List<Message>) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id
        appUserId?.let {
            cacheDao.insertMessagesWithRelated(messages.map {
                it.toMessageWithRelatedEntity(
                    dialogId = dialogId,
                    appUserId = appUserId
                )
            })
        }
    }

    @OptIn(InternalAPI::class)
    override suspend fun sendMessage(
        dialogId: String,
        message: String?,
        replyMessage: Message?,
        attachments: List<CachedFile>
    ): Response<Long> {


        if (message.isNullOrEmpty() && replyMessage == null && attachments.any()) {
            val result = dialogChatApi.sendAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.DialoguePeer(dialogId),
                    replyMessage?.id?.toInt()
                )
            )

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.any()) {
            val result =  dialogChatApi.sendMessageWithAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.DialoguePeer(dialogId),
                    replyMessage?.id?.toInt()
                )
            )
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.isEmpty()) {
            val result = dialogChatApi.sendMessage(
                body = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.DialoguePeer(dialogId),
                    replyMessage?.id?.toInt()
                )
            )
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        return Response.Error(ResponseError(message = "Неизвестное состояние"))
    }


    override suspend fun updateLastMessageOnPreview(dialogId: String, message: Message) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id
        appUserId?.let {
            dialogChatApi.readMessages(
                ChatReadRequestBody(
                    message.id.toInt(),
                    peer = IPeer_.DialoguePeer(userId = dialogId)
                )
            )
            historyCacheDaoNew.updateDialogLastMessage(
                appUserId = appUserId,
                dialogId = dialogId,
                message.toPreviewLastMessage(),
                null
            )
        }
    }


    override suspend fun getErredMessages(dialogId: String): List<SentUserMessage> {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id
        val messages = appUserId?.let {
             erredMsgDao.getErredMessageWithRelated(appUserId, dialogId)
                .map { it.toSentUserMessage() }
        }


        return messages.orEmpty()
    }

    override suspend fun setErredMessage(dialogId: String, message: SentUserMessage) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return
        val erredMessage =
            message.toErredMessageEntity(
                appUserId,
                dialogId
            )  ?: return
        erredMsgDao.addMessage(erredMessage)
        erredMsgDao.addCachedFiles(message.attachments.map { it.toEntity(message.id) })
    }

    override suspend fun delErredMessage(id: Long) = erredMsgDao.removeMessage(id)


    override suspend fun getUserMessage(dialogId: String): NewUserMessage {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return NewUserMessage()

        return userMsgDao.getUserMessageWithRelated(appUserId, dialogId)
            ?.toSentUserMessage() ?: NewUserMessage()

    }

    override suspend fun updateUserMessage(dialogId: String, message: NewUserMessage) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return

        userMsgDao.updateUserMessageWithRelated(
            message = message.toUserMessageEntity(appUserId, dialogId),
            files = message.attachments.map { it.toEntity(appUserId, dialogId) }
        )
    }


    override suspend fun updateFile(messageId: Long, attachment: MessageAttachment) {
        cacheDao.updateAttachments(
            loadProgress = attachment.loadProgress ?: 0f,
            idAttachment = attachment.id.toLong(),
            localFileUri = attachment.localFileUri.orEmpty()
        )
    }


}