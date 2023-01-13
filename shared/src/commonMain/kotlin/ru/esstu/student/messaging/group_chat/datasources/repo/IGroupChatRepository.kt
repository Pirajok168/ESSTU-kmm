package ru.esstu.student.messaging.group_chat.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.*
import ru.esstu.student.messaging.group_chat.entities.Conversation

interface IGroupChatRepository {
    suspend fun getHeader(id: Int): Flow<FlowResponse<Conversation>>
    suspend fun updateFile(messageId: Long, attachment: MessageAttachment)
    suspend fun getPage(convId: Int, limit: Int, offset: Int): Response<List<Message>>
    suspend fun setMessages(convId: Int, messages: List<Message>)
    suspend fun getUserMessage(convId: Int): NewUserMessage
    suspend fun updateUserMessage(convId: Int, message: NewUserMessage)

    suspend fun updateLastMessageOnPreview(convId: Int, message: Message)

    suspend fun sendMessage(
        convId: Int,
        message: String? = null,
        replyMessage: Message? = null,
        attachments: List<CachedFile> = emptyList()
    ): Response<Long>

    suspend fun getErredMessages(convId: Int): List<SentUserMessage>
    suspend fun setErredMessage(convId: Int, message: SentUserMessage)

    suspend fun delErredMessage(id: Long)
}