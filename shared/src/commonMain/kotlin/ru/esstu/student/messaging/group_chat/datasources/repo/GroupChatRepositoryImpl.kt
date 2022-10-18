package ru.esstu.student.messaging.group_chat.datasources.repo


import io.ktor.client.request.forms.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.Path.Companion.toPath

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.ConversionPeer
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatRequestBody
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.group_chat.datasources.*
import ru.esstu.student.messaging.group_chat.datasources.api.GroupChatApi
import ru.esstu.student.messaging.group_chat.entities.CachedFile
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.group_chat.entities.NewUserMessage
import ru.esstu.student.messaging.group_chat.entities.SentUserMessage




class GroupChatRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val groupChatApi: GroupChatApi,
) : IGroupChatRepository {

   // private val headerDao = groupChatDatabase.headerDao()

    override suspend fun getHeader(id: Int): Flow<FlowResponse<Conversation>> = flow {
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Error("unsupported user type")
            emit(FlowResponse.Loading())

            //val cachedHeader = headerDao.getConversationWithParticipants(appUserId = appUserId, id = id)?.toConversation()
           // if (cachedHeader != null)
           //     emit(FlowResponse.Success(cachedHeader))

            when (val response = auth.provideToken { type, access -> groupChatApi.getConversation("$access", id.toString()) }) {
                is Response.Error -> emit(FlowResponse.Error(response.error))
                is Response.Success -> {
                    val remoteHeader = response.data.toConversation()
                    if (remoteHeader == null)
                        emit(FlowResponse.Error(ResponseError(message = "Cast Exception")))
                    else {
                       // headerDao.setConversationWithParticipants(remoteHeader.toConversationWithParticipantsEntity(appUserId))
                       // if (remoteHeader != cachedHeader)
                            emit(FlowResponse.Success(remoteHeader))
                    }
                }
            }
            emit(FlowResponse.Loading(false))
        }
    }

    //private val cacheDao = groupChatDatabase.historyCacheDao()

    override suspend fun getPage(convId: Int, limit: Int, offset: Int): Response<List<Message>> {
        /*val cached = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Error("unsupported user type")

            cacheDao.getMessageHistory(
                limit = limit,
                offset = offset,
                conversationId = convId,
                appUserId = appUserId
            ).map { it.toMessage() }
        }*/

       // if (cached.data != null)
          //  return Response.Success(cached.data!!)

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

            //cacheDao.insertMessagesWithRelated(messages.map { it.toMessageWithRelated(conversationId = convId, appUserId = appUserId) })
        }
    }

    override suspend fun sendMessage(convId: Int, message: String?, replyMessage: Message?, attachments: List<CachedFile>): Response<Long> {
        val multipartBodyList = attachments.map {
            MultiPartFormDataContent(
                formData{
                    append("files", it.source.toPath().nameBytes.toByteArray())
                }
            )
        }

        if (message == null && replyMessage == null && attachments.any()) {
            val result = auth.provideToken { type, token ->
                groupChatApi.sendAttachments(
                    authToken = "$token",
                    files = multipartBodyList,
                    requestSendMessage = ChatRequestBody(peer = ConversionPeer(convId))
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
                    files = multipartBodyList,
                    requestSendMessage = ChatMessageRequestBody(message.orEmpty(), ConversionPeer(convId), replyMessage?.id?.toInt())
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
                    body = ChatMessageRequestBody(message.orEmpty(), ConversionPeer(convId), replyMessage?.id?.toInt())
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        return Response.Error(ResponseError(message = "Неизвестное состояние"))
    }

   /* private val conversationsDao = conversationDB.conversationDao()
    private val supportsDao = supportsDb.supportsDao()
    private val appealsDao = appealsDB.appealDao()*/

    override suspend fun updateLastMessageOnPreview(convId: Int, message: Message) {
        /*auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            groupChatApi.readMessages("${token.type} ${token.access}", ReadRequest(message.id.toInt(), peer = ConversionPeer(convId)))

            conversationsDao.updateConversationLastMessage(
                appUserId = appUserId,
                message = message.toMessageWithAttachments(),
                convId = convId
            )
            supportsDao.updateSupportLastMessage(
                convId = convId,
                message = message.toMessageWithAttachments(),
                appUserId = appUserId
            )
            appealsDao.updateAppealLastMessage(
                appUserId = appUserId,
                message = message.toMessageWithAttachments(),
                convId = convId
            )
        }*/
    }

    //private val erredMsgDao = groupChatDatabase.errorMessagesDao()

    override suspend fun getErredMessages(convId: Int): List<SentUserMessage> {
       /* val messages = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken null
            return@provideToken erredMsgDao.getErredMessageWithRelated(appUserId, convId).map { it.toSentUserMessage(context) }
        }.data ?: emptyList()

        return messages*/
        TODO()
    }

    override suspend fun setErredMessage(convId: Int, message: SentUserMessage) {
        /*auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken
            val erredMessage = message.toErredMessageEntity(appUserId, convId) ?: return@provideToken
            erredMsgDao.addMessage(erredMessage)
            erredMsgDao.addCachedFiles(message.attachments.map { it.toEntity(message.id) })
        }*/
        TODO()
    }

    override suspend fun delErredMessage(id: Long){

    } //= erredMsgDao.removeMessage(id)

    //private val userMsgDao = groupChatDatabase.userMessagesDao()

    override suspend fun getUserMessage(convId: Int): NewUserMessage {
       /* val message = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken null
            return@provideToken userMsgDao.getUserMessageWithRelated(appUserId, convId)?.toSentUserMessage(context)
        }.data ?: NewUserMessage()

        return message*/
        TODO()
    }

    override suspend fun updateUserMessage(convId: Int, message: NewUserMessage) {
       /* auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            userMsgDao.updateUserMessageWithRelated(
                message = message.toUserMessageEntity(appUserId, convId),
                files = message.attachments.map { it.toEntity(appUserId, convId) }
            )
        }*/
    }

   // private val historyCache = groupChatDatabase.historyCacheDao()
    override suspend fun updateFile(messageId: Long, attachment: MessageAttachment) {
       // historyCache.insertAttachments(listOf(attachment.toDialogChatAttachmentEntity(messageId)))
    }
}