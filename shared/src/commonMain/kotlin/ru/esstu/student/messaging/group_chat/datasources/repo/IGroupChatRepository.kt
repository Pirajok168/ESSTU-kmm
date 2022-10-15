package ru.esstu.student.messaging.group_chat.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.Attachment
import ru.esstu.student.messaging.entities.Message

import ru.esstu.student.messaging.group_chat.entities.CachedFile
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.group_chat.entities.NewUserMessage
import ru.esstu.student.messaging.group_chat.entities.SentUserMessage


interface IGroupChatRepository {
    suspend fun delErredMessage(id: Long)
    suspend fun updateFile(messageId: Long, attachment: Attachment)
    suspend fun getPage(convId: Int, limit: Int, offset: Int): Response<List<Message>>
    suspend fun setMessages(convId: Int, messages: List<Message>)
    suspend fun getUserMessage(convId: Int): NewUserMessage
    suspend fun updateUserMessage(convId: Int, message: NewUserMessage)
    suspend fun updateLastMessageOnPreview(convId: Int, message: Message)
    suspend fun getErredMessages(convId: Int): List<SentUserMessage>
    suspend fun setErredMessage(convId: Int, message: SentUserMessage)
    suspend fun sendMessage(convId: Int, message: String?, replyMessage: Message?, attachments: List<CachedFile>): Response<Long>
    suspend fun getHeader(id: Int): Flow<FlowResponse<Conversation>>
}