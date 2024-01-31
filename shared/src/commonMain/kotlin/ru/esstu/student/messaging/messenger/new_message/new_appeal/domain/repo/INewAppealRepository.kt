package ru.esstu.student.messaging.messenger.new_message.new_appeal.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme

interface INewAppealRepository {
    fun loadDepartments(): Flow<FlowResponse<List<AppealTheme>>>
    fun loadThemes(departmentId: String): Flow<FlowResponse<List<AppealTheme>>>

    suspend fun createNewAppeal(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview>

    suspend fun updateAppealOnPreview(appeal: ConversationPreview): Response<Unit>
}