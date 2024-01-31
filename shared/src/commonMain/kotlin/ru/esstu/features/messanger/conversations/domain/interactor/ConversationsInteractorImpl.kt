package ru.esstu.features.messanger.conversations.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.conversations.data.db.ConversationsCacheDao
import ru.esstu.features.messanger.conversations.data.repository.IConversationsApiRepository
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

class ConversationsInteractorImpl(
    val repo: IConversationsApiRepository,
    val dao: ConversationsCacheDao
) : ConversationsInteractor {
    override suspend fun getLocalConversation(limit: Int, offset: Int): List<ConversationPreview> =
        dao.getConversations(limit, offset)

    override suspend fun getConversation(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> =
        repo.getConversations(limit, offset)
            .doOnSuccess {
                it.map {
                    dao.setConversation(it)
                }
            }

    override suspend fun clearConversation() = dao.clearDialogs()
}