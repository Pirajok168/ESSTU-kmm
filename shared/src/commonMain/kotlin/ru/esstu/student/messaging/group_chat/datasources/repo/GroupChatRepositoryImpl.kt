package ru.esstu.student.messaging.group_chat.datasources.repo

import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.IPeer_
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.dialog_chat.datasources.toMessages
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.NewUserMessage
import ru.esstu.student.messaging.group_chat.datasources.*
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.GroupChatHistoryCacheDao
import ru.esstu.student.messaging.group_chat.datasources.db.header.HeaderDao
import ru.esstu.student.messaging.group_chat.datasources.db.user_messages.GroupUserMessageDao
import ru.esstu.student.messaging.group_chat.entities.Conversation

class GroupChatRepositoryImpl constructor(
    private val auth: IAuthRepository,
    private val groupChatApi: GroupChatApi,
    private val historyCacheDao: GroupChatHistoryCacheDao,
    private val headerDao: HeaderDao,
    private val userMsgDao: GroupUserMessageDao
): IGroupChatRepository {
    override suspend fun getHeader(id: Int): Flow<FlowResponse<Conversation>> = flow{
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Error("unsupported user type")
            emit(FlowResponse.Loading())

            val cachedHeader = headerDao.getConversationWithParticipants(appUserId = appUserId, id = id)?.toConversation()
            if (cachedHeader != null)
               emit(FlowResponse.Success(cachedHeader))

            when (val response = auth.provideToken { type, access -> groupChatApi.getConversation("$access", id.toString()) }) {
                is Response.Error -> emit(FlowResponse.Error(response.error))
                is Response.Success -> {
                    val remoteHeader = response.data.toConversation()
                    if (remoteHeader == null)
                        emit(FlowResponse.Error(ResponseError(message = "Cast Exception")))
                    else {
                        headerDao.setConversationWithParticipants(remoteHeader.toConversationWithParticipantsEntity(appUserId))
                        if (remoteHeader != cachedHeader)
                            emit(FlowResponse.Success(remoteHeader))
                    }
                }
            }
            emit(FlowResponse.Loading(false))
        }
    }

    override suspend fun updateFile(messageId: Long, attachment: MessageAttachment) {
        TODO("Not yet implemented")
    }

    override suspend fun getPage(
        convId: Int,
        limit: Int,
        offset: Int
    ): Response<List<Message>> {
        val cached = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Error("unsupported user type")

            historyCacheDao.getMessageHistory(
                limit = limit,
                offset = offset,
                conversationId = convId.toLong(),
                appUserId = appUserId
            ).map { it.toMessage() }
        }

        if (!cached.data.isNullOrEmpty())
            return Response.Success(cached.data!!)

        val remotePage = auth.provideToken { type, token ->
            val rawPage = groupChatApi.getHistory("$token", convId.toString(), offset, limit)

            rawPage.toMessages(
                provideReplies = { indices ->
                    groupChatApi.pickMessages("$token", indices.joinToString())
                },
                provideUsers = { indices ->
                    groupChatApi.pickUsers("$token", indices.joinToString())
                }
            )
        }

        return when (remotePage) {
            is Response.Error -> Response.Error(remotePage.error)
            is Response.Success -> {
                setMessages(convId, remotePage.data)
                Response.Success(remotePage.data)
            }
        }
    }

    override suspend fun setMessages(convId: Int, messages: List<Message>) {
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            historyCacheDao.insertMessagesWithRelated(messages.map {
                it.toMessageWithRelatedGroupChat(
                    conversationId = convId,
                    appUserId = appUserId,
                )
            })
        }
    }

    override suspend fun getUserMessage(convId: Int): NewUserMessage {

        val message = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken null
            Napier.e("$appUserId - $convId")
            val a = userMsgDao.getUserMessageWithRelated(appUserId, convId.toLong())?.toSentUserMessage()
            a
            return@provideToken a
        }

        when (message){
            is Response.Error -> {
                Napier.e(message.error.message.toString())
            }
            is Response.Success -> {
                return message.data ?: NewUserMessage()
            }
        }



        return  NewUserMessage()
    }

    override suspend fun updateUserMessage(convId: Int, message: NewUserMessage) {
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            userMsgDao.updateUserMessageWithRelated(
                message = message.toUserMessageEntity(appUserId, convId),
                files = message.attachments.map { it.toEntity(appUserId, convId) }
            )
        }
    }

    override suspend fun updateLastMessageOnPreview(convId: Int, message: Message) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(
        convId: Int,
        message: String?,
        replyMessage: Message?,
        attachments: List<CachedFile>
    ): Response<Long> {
        if (message.isNullOrEmpty() && replyMessage == null && attachments.any()) {
            val result = auth.provideToken { type, token ->
                groupChatApi.sendAttachments(
                    authToken = "$token",
                    files = attachments,
                    requestSendMessage = ChatMessageRequestBody(
                        message.orEmpty(),
                        IPeer_.ConversionPeer(convId),
                        replyMessage?.id?.toInt()
                    )
                )
            }

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.any()) {
            val result = auth.provideToken { type, token ->
                groupChatApi.sendMessageWithAttachments(
                    authToken = "$token",
                    files = attachments,
                    requestSendMessage = ChatMessageRequestBody(
                        message.orEmpty(),
                        IPeer_.ConversionPeer(convId),
                        replyMessage?.id?.toInt()
                    )
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.isEmpty()) {
            val result = auth.provideToken { type, token ->
                groupChatApi.sendMessage(
                    authToken = "$token",
                    body = ChatMessageRequestBody(
                        message.orEmpty(),
                        IPeer_.ConversionPeer(convId),
                        replyMessage?.id?.toInt()
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
}