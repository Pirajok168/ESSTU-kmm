package ru.esstu.features.messanger.supports.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview
import ru.esstu.features.messanger.supports.data.db.SupportsCacheDao
import ru.esstu.features.messanger.supports.data.repository.ISupportsApiRepository

class SupportInteractorImpl(
    val repo: ISupportsApiRepository,
    val dao: SupportsCacheDao
) : SupportInteractor {
    override suspend fun getLocalSupports(limit: Int, offset: Int): List<ConversationPreview> =
        dao.getSupports(limit, offset)

    override suspend fun getSupports(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> =
        repo.getSupports(limit, offset)
            .doOnSuccess {
                it.map {
                    dao.setSupport(it)
                }
            }

    override suspend fun clearSupports() = dao.clearSupports()
}