package ru.esstu.student.messaging.messenger.new_message.new_support.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

interface INewSupportRepository {
    fun getSupportThemes(): Flow<FlowResponse<List<SupportTheme>>>

    suspend fun createNewSupport(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview>

    suspend fun updateSupportsOnPreview(support: ConversationPreview): Response<Unit>
}