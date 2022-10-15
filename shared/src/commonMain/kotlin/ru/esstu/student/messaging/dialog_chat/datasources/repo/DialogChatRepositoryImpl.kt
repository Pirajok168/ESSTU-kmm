package ru.esstu.student.messaging.dialog_chat.datasources.repo


import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.Path.Companion.toPath

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.peer.DialoguePeer
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatMessageRequestBody
import ru.esstu.domain.datasources.esstu_rest_dtos.esstu.request.chat_message_request.request_body.ChatRequestBody
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.student.messaging.dialog_chat.datasources.*
import ru.esstu.student.messaging.dialog_chat.datasources.api.DialogChatApi
import ru.esstu.student.messaging.dialog_chat.datasources.api.request.ReadRequest
import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import ru.esstu.student.messaging.dialog_chat.entities.NewUserMessage
import ru.esstu.student.messaging.dialog_chat.entities.SentUserMessage
import ru.esstu.student.messaging.entities.Attachment
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.User
import ru.esstu.student.messaging.messenger.datasources.toUser




class DialogChatRepositoryImpl constructor(

   // private val context: Context,
    private val auth: IAuthRepository,
    private val dialogChatApi: DialogChatApi,
    //dialogChatDatabase: DialogChatDatabase,
    //dialogsDb: DialogsDatabase
) : IDialogChatRepository {

    //private val opponentDao = dialogChatDatabase.opponentDao()

    override suspend fun getOpponent(id: String): Flow<FlowResponse<User>> = flow {
        emit(FlowResponse.Loading())

        //val cachedOpponent = opponentDao.getOpponent(id)?.toUser()
       // if (cachedOpponent != null)
       //     emit(FlowResponse.Success(cachedOpponent))

        when (val response = auth.provideToken { type, token -> dialogChatApi.getOpponent("$token", id) }) {
            is Response.Error -> emit(FlowResponse.Error(response.error))
            is Response.Success -> {
                val remoteOpponent = response.data.user.toUser()
                if (remoteOpponent == null)
                    emit(FlowResponse.Error(ResponseError(message = "Cast Exception")))
                else {
               //     opponentDao.insert(remoteOpponent.toUserEntity())
                 //   if (remoteOpponent != cachedOpponent)
                        emit(FlowResponse.Success(remoteOpponent))
                }
            }
        }
        emit(FlowResponse.Loading(false))
    }

    //private val cacheDao = dialogChatDatabase.historyCacheDao()

    override suspend fun getPage(dialogId: String, limit: Int, offset: Int): Response<List<Message>> {
        /*val cached = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Error("unsupported user type")

            cacheDao.getMessageHistory(
                limit = limit,
                offset = offset,
                opponentId = dialogId,
                appUserId = appUserId
            ).map { it.toMessage() }
        }*/

       // if (cached.data != null)
           // return Response.Success(cached.data!!)

        val remotePage = auth.provideToken { type, token ->
            val rawPage = dialogChatApi.getHistory("$type $token", dialogId, offset, limit)

            rawPage.toMessages(
                provideReplies = { indices ->
                    dialogChatApi.pickMessages("$token", indices.joinToString())
                },
                provideUsers = { indices ->
                    dialogChatApi.pickUsers("$token", indices.joinToString())
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
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            //cacheDao.insertMessagesWithRelated(messages.map { it.toMessageWithRelated(dialogId = dialogId, appUserId = appUserId) })
        }
    }

    override suspend fun sendMessage(dialogId: String, message: String?, replyMessage: Message?, attachments: List<CachedFile>): Response<Long> {
        val multipartBodyList = attachments.map {
            MultiPartFormDataContent(
                formData{
                    append("files", it.sourceFile.toPath().nameBytes.toByteArray())
                }
            )
        }





        if (message == null && replyMessage == null && attachments.any()) {
            val result = auth.provideToken { type, token ->
                dialogChatApi.sendAttachments(
                    authToken = "$token",
                    files = multipartBodyList,
                    requestSendMessage = ChatRequestBody(peer = DialoguePeer(userId = dialogId))
                )
            }

            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.any()) {
            val result = auth.provideToken { type, token ->
                dialogChatApi.sendMessageWithAttachments(
                    authToken = "$token",
                    files = multipartBodyList,
                    requestSendMessage = ChatMessageRequestBody(message.orEmpty(), DialoguePeer(dialogId), replyMessage?.id?.toInt())
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        if ((message != null || replyMessage != null) && attachments.isEmpty()) {
            val result = auth.provideToken { type, token ->
                dialogChatApi.sendMessage(
                    authToken = "$token",
                    body = ChatMessageRequestBody(message.orEmpty(), DialoguePeer(dialogId), replyMessage?.id?.toInt())
                )
            }
            return when (result) {
                is Response.Error -> Response.Error(result.error)
                is Response.Success -> Response.Success(result.data.id)
            }
        }

        return Response.Error(ResponseError(message = "Неизвестное состояние"))
    }

    //private val dialogsDao = dialogsDb.cacheDao()

    override suspend fun updateLastMessageOnPreview(dialogId: String, message: Message) {
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            dialogChatApi.readMessages("${token.access}", ReadRequest(message.id.toInt(), peer = DialoguePeer(userId = dialogId)))

            //dialogsDao.updateDialogLastMessage(appUserId = appUserId, dialogId = dialogId, message = message.toMessageWithAttachments())
        }
    }

    //private val erredMsgDao = dialogChatDatabase.errorMessagesDao()

    override suspend fun getErredMessages(dialogId: String): List<SentUserMessage> {
         TODO()
    /*val messages = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken null
            return@provideToken erredMsgDao.getErredMessageWithRelated(appUserId, dialogId).map { it.toSentUserMessage(context) }
        }.data ?: emptyList()

        return messages*/
    }

    override suspend fun setErredMessage(dialogId: String, message: SentUserMessage) {
        /*auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken
            val erredMessage = message.toErredMessageEntity(appUserId, dialogId) ?: return@provideToken
            erredMsgDao.addMessage(erredMessage)
            erredMsgDao.addCachedFiles(message.attachments.map { it.toEntity(message.id) })
        }*/
    }

    override suspend fun delErredMessage(id: Long){

    }
    //= erredMsgDao.removeMessage(id)

    //private val userMsgDao = dialogChatDatabase.userMessagesDao()

    override suspend fun getUserMessage(dialogId: String): NewUserMessage {
        /*val message = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken null
            return@provideToken userMsgDao.getUserMessageWithRelated(appUserId, dialogId)?.toSentUserMessage(context)
        }.data ?: NewUserMessage()

        return message*/
        TODO()
    }

    override suspend fun updateUserMessage(dialogId: String, message: NewUserMessage) {
       /* auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: return@provideToken

            userMsgDao.updateUserMessageWithRelated(
                message = message.toUserMessageEntity(appUserId, dialogId),
                files = message.attachments.map { it.toEntity(appUserId, dialogId) }
            )
        }*/
    }

    //private val historyCache = dialogChatDatabase.historyCacheDao()
    override suspend fun updateFile(messageId: Long, attachment: Attachment) {
       // historyCache.insertAttachments(listOf(attachment.toDialogChatAttachmentEntity(messageId)))
    }
}