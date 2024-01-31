package ru.esstu.student.messaging.dialog_chat.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.entities.Message
import ru.esstu.student.messaging.entities.MessageAttachment
import ru.esstu.student.messaging.entities.NewUserMessage
import ru.esstu.student.messaging.entities.Sender
import ru.esstu.student.messaging.entities.SentUserMessage

interface IDialogChatRepository {
    suspend fun getOpponent(id: String): Flow<FlowResponse<Sender>>

    suspend fun getPage(dialogId: String, limit: Int, offset: Int): Response<List<Message>>
    suspend fun setMessages(dialogId: String, messages: List<Message>)
    suspend fun sendMessage(
        dialogId: String,
        message: String? = null,
        replyMessage: Message? = null,
        attachments: List<CachedFile> = emptyList()
    ): Response<Long>

    suspend fun updateLastMessageOnPreview(dialogId: String, message: Message)
    suspend fun getErredMessages(dialogId: String): List<SentUserMessage>
    suspend fun setErredMessage(dialogId: String, message: SentUserMessage)
    suspend fun delErredMessage(id: Long)
    suspend fun getUserMessage(dialogId: String): NewUserMessage
    suspend fun updateUserMessage(dialogId: String, message: NewUserMessage)
    suspend fun updateFile(messageId: Long, attachment: MessageAttachment)

}