package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.toToken
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDao
import ru.esstu.student.messaging.messenger.appeals.toAppeals
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.NewAppealApi
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.toAppeal
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme

class NewAppealRepositoryImpl(
    private val api: NewAppealApi,
    private val appealsCacheDao: AppealsCacheDao,
    private val loginDataRepository: ILoginDataRepository,
) : INewAppealRepository {
    override fun loadDepartments(): Flow<FlowResponse<List<AppealTheme>>> = flow {
        emit(FlowResponse.Loading())
        val result = api.getDepartments().transform { it.map { it.toAppeal() } }
            .doOnSuccess {
                emit(FlowResponse.Success(it))
            }

        if (result is Response.Error)
            emit(FlowResponse.Error(result.error))

        emit(FlowResponse.Loading(false))
    }

    override fun loadThemes(departmentId: String): Flow<FlowResponse<List<AppealTheme>>> = flow {
        emit(FlowResponse.Loading())

        api.getDepartmentsThemes(departmentId).transform { it.map { it.toAppeal() } }
            .doOnSuccess {
                emit(FlowResponse.Success(it))
            }


        emit(FlowResponse.Loading(false))
    }

    override suspend fun createNewAppeal(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview> {
        val result = if (message.isNotBlank() && attachments.isNotEmpty()) {
            api.createAppealChatWithAttachments(
                files = attachments,
                body = NewAppealRequestBody(
                    message = message,
                    users = listOf(themeId),
                    type = "SUPPORT"
                )
            ).transform { it.toAppeals().firstOrNull() }
        } else if (message.isNotBlank()) {
            api.createAppealChat(
                body = NewAppealRequestBody(
                    message = message,
                    users = listOf(themeId),
                    type = "SUPPORT"
                )
            ).transform { it.toAppeals().firstOrNull() }
        } else if (attachments.isNotEmpty()) {
            api.createAppealChatWithAttachments(
                files = attachments,
                body = NewAppealRequestBody(
                    users = listOf(themeId),
                    type = "SUPPORT",
                    message = ""
                )
            ).transform { it.toAppeals().firstOrNull() }
        } else
            throw Exception("неизвестное состояние")
        return when (result) {
            is Response.Error -> Response.Error(result.error)
            is Response.Success -> result.data?.let { Response.Success(it) } ?: Response.Error(
                ResponseError(error = ServerErrors.Unknown)
            )
        }
    }

    override suspend fun updateAppealOnPreview(appeal: ConversationPreview): Response<Unit> {
        val appUserId = loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return Response.Error(ResponseError(error = ServerErrors.Unknown))
        appealsCacheDao.updateDialogLastMessage(
            appUserId,
            appeal.id,
            appeal.lastMessage ?: return Response.Error(ResponseError()),
            appeal,
            true
        )
        return Response.Success(Unit)
    }
}