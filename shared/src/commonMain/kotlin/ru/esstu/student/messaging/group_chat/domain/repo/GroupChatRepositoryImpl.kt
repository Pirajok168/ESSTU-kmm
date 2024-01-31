package ru.esstu.student.messaging.group_chat.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.data.token.repository.ILoginDataRepository
import ru.esstu.data.token.toToken
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.api.model.ServerErrors
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatReadRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_
import ru.esstu.features.messanger.appeal.data.db.AppealsCacheDao
import ru.esstu.features.messanger.conversations.data.db.ConversationsCacheDao
import ru.esstu.features.messanger.supports.data.db.SupportsCacheDao
import ru.esstu.student.messaging.dialog_chat.domain.toMessages
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.NewUserMessage
import ru.esstu.student.messaging.entities.SentUserMessage
import ru.esstu.student.messaging.group_chat.data.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.data.db.chat_history.GroupChatHistoryCacheDao
import ru.esstu.student.messaging.group_chat.data.db.erred_messages.ErredMessageDao
import ru.esstu.student.messaging.group_chat.data.db.header.HeaderDao
import ru.esstu.student.messaging.group_chat.data.db.user_messages.GroupUserMessageDao
import ru.esstu.student.messaging.group_chat.domain.toConversation
import ru.esstu.student.messaging.group_chat.domain.toConversationWithParticipantsEntity
import ru.esstu.student.messaging.group_chat.domain.toEntity
import ru.esstu.student.messaging.group_chat.domain.toErredMessageEntity
import ru.esstu.student.messaging.group_chat.domain.toMessage
import ru.esstu.student.messaging.group_chat.domain.toMessageWithRelatedGroupChat
import ru.esstu.student.messaging.group_chat.domain.toSentUserMessage
import ru.esstu.student.messaging.group_chat.domain.toUserMessageEntity
import ru.esstu.student.messaging.group_chat.entities.Conversation

class GroupChatRepositoryImpl(
    private val groupChatApi: GroupChatApi,
    private val historyCacheDao: GroupChatHistoryCacheDao,
    private val cache: ConversationsCacheDao,
    private val headerDao: HeaderDao,
    private val userMsgDao: GroupUserMessageDao,
    private val erredMsgDao: ErredMessageDao,
    private val supportMsgDao: SupportsCacheDao,
    private val appealMsgDao: AppealsCacheDao,
    private val loginDataRepository: ILoginDataRepository,
) : IGroupChatRepository {
    override suspend fun getHeader(id: Int): Flow<FlowResponse<Conversation>> = flow {
        loginDataRepository.getAccessToken()?.toToken()?.owner?.id?.let { appUserId ->
            emit(FlowResponse.Loading())

            val cachedHeader =
                headerDao.getConversationWithParticipants(appUserId = appUserId, id = id)
                    ?.toConversation()
            if (cachedHeader != null)
                emit(FlowResponse.Success(cachedHeader))

            when (val response = groupChatApi.getConversation(id.toString())) {
                is Response.Error -> emit(FlowResponse.Error(response.error))
                is Response.Success -> {
                    val remoteHeader = response.data.toConversation()
                    headerDao.setConversationWithParticipants(
                        remoteHeader.toConversationWithParticipantsEntity(
                            appUserId
                        )
                    )
                    if (remoteHeader != cachedHeader)
                        emit(FlowResponse.Success(remoteHeader))
                }
            }
            emit(FlowResponse.Loading(false))
        }
    }

    override suspend fun updateFile(messageId: Long, attachment: MessageAttachment) {
        historyCacheDao.updateAttachments(
            loadProgress = attachment.loadProgress ?: 0f,
            idAttachment = attachment.id.toLong(),
            localFileUri = attachment.localFileUri.orEmpty()
        )
    }

    override suspend fun getPage(
        convId: Int,
        limit: Int,
        offset: Int
    ): Response<List<Message>> {
        val appUserId =
            loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return Response.Error(
                error = ResponseError(error = ServerErrors.Unauthorized)
            )
        val cached = historyCacheDao.getMessageHistory(
            limit = limit,
            offset = offset,
            conversationId = convId.toLong(),
            appUserId = appUserId
        ).map { it.toMessage() }

        if (cached.isNotEmpty())
            return Response.Success(cached)

        val remotePage = groupChatApi.getHistory(convId.toString(), offset, limit)
            .transform {
                it.toMessages(
                    provideReplies = { indices ->
                        groupChatApi.pickMessages(indices.joinToString()).data.orEmpty()
                    },
                    provideUsers = { indices ->
                        groupChatApi.pickUsers(indices.joinToString()).data.orEmpty()
                    }
                )
            }
            .doOnSuccess {
                setMessages(convId, it)
            }



        return remotePage
    }

    override suspend fun setMessages(convId: Int, messages: List<Message>) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return
        historyCacheDao.insertMessagesWithRelated(messages.map {
            it.toMessageWithRelatedGroupChat(
                conversationId = convId,
                appUserId = appUserId,
            )
        })
    }

    override suspend fun getUserMessage(convId: Int): NewUserMessage {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id

        val message = appUserId?.let {
            userMsgDao.getUserMessageWithRelated(appUserId, convId.toLong())?.toSentUserMessage()
        }


        return message ?: NewUserMessage()
    }

    override suspend fun updateUserMessage(convId: Int, message: NewUserMessage) {
        loginDataRepository.getAccessToken()?.toToken()?.owner?.id?.let { id ->
            userMsgDao.updateUserMessageWithRelated(
                message = message.toUserMessageEntity(id, convId),
                files = message.attachments.map { it.toEntity(id, convId) }
            )
        }
    }

    override suspend fun updateLastMessageOnPreview(convId: Int, message: Message) {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return
        groupChatApi.readMessages(
            ChatReadRequestBody(
                message.id.toInt(),
                peer = IPeer_.ConversionPeer(convId)
            )
        )


    }

    override suspend fun sendMessage(
        convId: Int,
        message: String?,
        replyMessage: Message?,
        attachments: List<CachedFile>
    ): Response<Long> {
        if (message.isNullOrEmpty() && replyMessage == null && attachments.any()) {
            val result = groupChatApi.sendAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.ConversionPeer(convId),
                    replyMessage?.id?.toInt()
                )
            )

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.any()) {
            val result = groupChatApi.sendMessageWithAttachments(
                files = attachments,
                requestSendMessage = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.ConversionPeer(convId),
                    replyMessage?.id?.toInt()
                )
            )
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.isEmpty()) {
            val result = groupChatApi.sendMessage(
                body = ChatMessageRequestBody(
                    message.orEmpty(),
                    IPeer_.ConversionPeer(convId),
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

    override suspend fun getErredMessages(convId: Int): List<SentUserMessage> {
        val appUserId =
            loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return emptyList()

        return erredMsgDao.getErredMessageWithRelated(appUserId, convId)
            .map { it.toSentUserMessage() }
    }

    override suspend fun setErredMessage(convId: Int, message: SentUserMessage) {
        val appUserId =
            loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return

        val erredMessage = message.toErredMessageEntity(appUserId, convId) ?: return
        erredMsgDao.addMessage(erredMessage)
        erredMsgDao.addCachedFiles(message.attachments.map { it.toEntity(message.id) })

    }

    override suspend fun delErredMessage(id: Long) = erredMsgDao.removeMessage(id)
}