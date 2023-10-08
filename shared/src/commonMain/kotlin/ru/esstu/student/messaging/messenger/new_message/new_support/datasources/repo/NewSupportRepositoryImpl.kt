package ru.esstu.student.messaging.messenger.new_message.new_support.datasources.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.domain.utill.wrappers.FlowResponse
import ru.esstu.domain.utill.wrappers.Response
import ru.esstu.student.messaging.entities.CachedFile
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.NewSupportApi
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.api.request.NewSupportRequestBody
import ru.esstu.student.messaging.messenger.new_message.new_support.datasources.toSupportGroup
import ru.esstu.student.messaging.messenger.new_message.new_support.entities.SupportTheme
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsCacheDao
import ru.esstu.student.messaging.messenger.supports.toSupports

class NewSupportRepositoryImpl(
    private val auth: IAuthRepository,
    private val api: NewSupportApi,
    private val cashSupportsCacheDao: SupportsCacheDao
): INewSupportRepository {
    override fun getSupportThemes(): Flow<FlowResponse<List<SupportTheme>>>  = flow{
        emit(FlowResponse.Loading())
        val result = auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported user type")

            val responseGroups = api.getSupport("${token.access}").map { it.toSupportGroup() }
            emit(FlowResponse.Success(responseGroups))
        }

        if (result is Response.Error)
            emit(FlowResponse.Error(result.error))

        emit(FlowResponse.Loading(false))
    }

    override suspend fun createNewSupport(
        themeId: String,
        message: String,
        attachments: List<CachedFile>
    ): Response<ConversationPreview> {
        return auth.provideToken {  type, token ->
            if (message.isNotBlank() && attachments.isNotEmpty()){
                api.createSupportChatWithAttachments(
                    authToken = token,
                    files = attachments,
                    body = NewSupportRequestBody(
                        message = message,
                        users = listOf(themeId),
                        type = "SUPPORT"
                    )
                ).toSupports().firstOrNull() ?: throw Exception("cast exception")
            }else if (message.isNotBlank()){
                api.createSupportChat(
                    authToken = token,
                    body = NewSupportRequestBody(
                        message = message,
                        users = listOf(themeId),
                        type = "SUPPORT"
                    )
                ).toSupports().firstOrNull() ?: throw Exception("cast exception")
            }else if (attachments.isNotEmpty()){
                api.createSupportChatWithAttachments(
                    authToken = token,
                    body = NewSupportRequestBody(
                        users = listOf(themeId),
                        type = "SUPPORT"
                    ),
                    files = attachments
                ).toSupports().firstOrNull() ?: throw Exception("cast exception")
            } else
                throw Exception("неизвестное состояние")
        }
    }

    override suspend fun updateSupportsOnPreview(support: ConversationPreview): Response<Unit> {
       return auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
           cashSupportsCacheDao.updateDialogLastMessage(
               appUserId,
               support.id,
               support.lastMessage ?: return@provideToken,
               support,
               true
           )
       }


    }
}