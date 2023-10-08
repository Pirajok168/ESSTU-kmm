package ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDao
import ru.esstu.student.messaging.messenger.appeals.toAppeals
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.NewAppealApi
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.api.request.NewAppealRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_appeal.datasources.toAppeal
import ru.esstu.student.messaging.messenger.new_message.new_appeal.entities.AppealTheme
import ru.esstu.student.messaging.messenger.supports.toSupports

class NewAppealRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: NewAppealApi,
    private val appealsCacheDao: AppealsCacheDao
): INewAppealRepository {
    override fun loadDepartments(): Flow<FlowResponse<List<AppealTheme>>>  = flow{
        emit(FlowResponse.Loading())
        val result = auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported user type")

            val responseGroups = api.getDepartments("${token.access}").map { it.toAppeal() }
            emit(FlowResponse.Success(responseGroups))
        }

        if (result is Response.Error)
            emit(FlowResponse.Error(result.error))

        emit(FlowResponse.Loading(false))
    }

    override fun loadThemes(departmentId: String): Flow<FlowResponse<List<AppealTheme>>> = flow{
        emit(FlowResponse.Loading())

        auth.provideToken { token ->
            val appUserId = token.owner.id
            val authToken = "${token.access}"

            val remoteThemes = api.getDepartmentsThemes(authToken, departmentId).map { it.toAppeal() }
            emit(FlowResponse.Success(remoteThemes))
        }

        emit(FlowResponse.Loading(false))
    }

    override suspend fun createNewAppeal(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview> {
        return auth.provideToken {  type, token ->
            if (message.isNotBlank() && attachments.isNotEmpty()){
                api.createAppealChatWithAttachments(
                    authToken = token,
                    files = attachments,
                    body = NewAppealRequestBody(
                        message = message,
                        users = listOf(themeId),
                        type = "SUPPORT"
                    )
                ).toAppeals().firstOrNull() ?: throw Exception("cast exception")
            }else if (message.isNotBlank()){
                api.createAppealChat(
                    authToken = token,
                    body = NewAppealRequestBody(
                        message = message,
                        users = listOf(themeId),
                        type = "SUPPORT"
                    )
                ).toAppeals().firstOrNull() ?: throw Exception("cast exception")
            }else if (attachments.isNotEmpty()){
                api.createAppealChatWithAttachments(
                    authToken = token,
                    body = NewAppealRequestBody(
                        users = listOf(themeId),
                        type = "SUPPORT",
                        message = ""
                    ),
                    files = attachments
                ).toAppeals().firstOrNull() ?: throw Exception("cast exception")
            } else
                throw Exception("неизвестное состояние")
        }
    }

    override suspend fun updateAppealOnPreview(appeal: ConversationPreview): Response<Unit> {
        return auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
            appealsCacheDao.updateDialogLastMessage(
                appUserId,
                appeal.id,
                appeal.lastMessage ?: return@provideToken,
                appeal,
                true
            )
        }
    }
}