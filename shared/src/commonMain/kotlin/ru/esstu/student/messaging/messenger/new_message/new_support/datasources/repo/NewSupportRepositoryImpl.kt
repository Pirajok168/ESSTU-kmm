package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.local.ILoginDataRepository
import ru.esstu.auth.datasources.toToken
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.domain.utill.wrappers.ResponseError
import ru.esstu.domain.utill.wrappers.ServerErrors
import ru.esstu.domain.utill.wrappers.doOnError
import ru.esstu.domain.utill.wrappers.doOnSuccess
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.toSupportGroup
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsCacheDao
import ru.esstu.student.messaging.messenger.supports.toSupports

class NewSupportRepositoryImpl(
    private val api: NewSupportApi,
    private val cashSupportsCacheDao: SupportsCacheDao,
    private val loginDataRepository: ILoginDataRepository,
) : INewSupportRepository {
    override fun getSupportThemes(): Flow<FlowResponse<List<SupportTheme>>> = flow {
        emit(FlowResponse.Loading())
        api.getSupport()
            .transform {
                it.map { it.toSupportGroup() }
            }
            .doOnSuccess {
                emit(FlowResponse.Success(it))
            }
            .doOnError {
                emit(FlowResponse.Error(it))
            }

        emit(FlowResponse.Loading(false))
    }

    override suspend fun createNewSupport(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview> {
        val response = if (message.isNotBlank() && attachments.isNotEmpty()) {
            api.createSupportChatWithAttachments(
                files = attachments,
                body = NewSupportRequestBody(
                    message = message,
                    users = listOf(themeId),
                    type = "SUPPORT"
                )
            )
                .transform {
                    it.toSupports().firstOrNull()
                }

        } else if (message.isNotBlank()) {
            api.createSupportChat(
                body = NewSupportRequestBody(
                    message = message,
                    users = listOf(themeId),
                    type = "SUPPORT"
                )
            ).transform {
                it.toSupports().firstOrNull()
            }
        } else if (attachments.isNotEmpty()) {
            api.createSupportChatWithAttachments(
                files = attachments,
                body = NewSupportRequestBody(
                    users = listOf(themeId),
                    type = "SUPPORT"
                )
            ).transform {
                it.toSupports().firstOrNull()
            }
        } else
            throw Exception("неизвестное состояние")

        return when (response) {
            is Response.Error -> Response.Error(response.error)
            is Response.Success -> response.data?.let { Response.Success(it) } ?: Response.Error(
                ResponseError(error = ServerErrors.Unknown)
            )
        }
    }

    override suspend fun updateSupportsOnPreview(support: ConversationPreview): Response<Unit> {
        val appUserId =
            loginDataRepository.getAccessToken()?.toToken()?.owner?.id ?: return Response.Error(
                ResponseError(error = ServerErrors.Unknown)
            )
        cashSupportsCacheDao.updateDialogLastMessage(
            appUserId,
            support.id,
            support.lastMessage ?: return Response.Error(
                ResponseError(error = ServerErrors.Unknown)
            ),
            support,
            true
        )

        return Response.Success(Unit)

    }
}