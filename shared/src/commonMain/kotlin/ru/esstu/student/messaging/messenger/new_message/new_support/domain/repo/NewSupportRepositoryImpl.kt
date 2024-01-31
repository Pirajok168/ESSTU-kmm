package ru.esstu.student.messaging.messenger.new_message.new_support.domain.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.data.token.repository.ILoginDataRepository
import ru.esstu.data.token.toToken
import ru.esstu.data.web.api.model.FlowResponse
import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.ResponseError
import ru.esstu.data.web.api.model.ServerErrors
import ru.esstu.data.web.api.model.doOnError
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.supports.data.db.SupportsCacheDao
import ru.esstu.features.messanger.supports.domain.toSupports
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.data.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.domain.toSupportGroup
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme

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


        return Response.Success(Unit)

    }
}