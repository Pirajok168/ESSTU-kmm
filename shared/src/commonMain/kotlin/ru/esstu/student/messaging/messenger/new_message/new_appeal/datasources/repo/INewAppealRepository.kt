package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo

import kotlinx.coroutines.flow.Flow
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme

interface INewAppealRepository {
    fun loadDepartments(): Flow<FlowResponse<List<AppealTheme>>>
    fun loadThemes(departmentId: String): Flow<FlowResponse<List<AppealTheme>>>

    suspend fun createNewAppeal(themeId: String, message: String, attachments: List<CachedFile>): Response<ConversationPreview>
    suspend fun updateAppealOnPreview(appeal: ConversationPreview): Response<Unit>
}