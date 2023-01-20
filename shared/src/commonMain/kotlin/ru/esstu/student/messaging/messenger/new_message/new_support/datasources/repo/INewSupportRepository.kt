package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.group_chat.entities.Conversation
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

interface INewSupportRepository {
    fun getSupportThemes(): Flow<FlowResponse<List<SupportTheme>>>

    suspend fun createNewSupport(themeId: String, message: String, attachments: List<CachedFile>): Response<ConversationPreview>

    suspend fun updateSupportsOnPreview(support: ConversationPreview): Response<Unit>
}