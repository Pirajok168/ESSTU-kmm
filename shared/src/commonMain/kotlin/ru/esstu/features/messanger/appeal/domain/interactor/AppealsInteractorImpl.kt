package ru.esstu.features.messanger.appeal.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.appeal.data.db.AppealsCacheDao
import ru.esstu.features.messanger.appeal.data.repository.IAppealsApiRepository
import ru.esstu.features.messanger.conversations.domain.model.ConversationPreview

class AppealsInteractorImpl(
    val repo: IAppealsApiRepository,
    val dao: AppealsCacheDao
) : AppealsInteractor {
    override suspend fun getLocalAppeals(limit: Int, offset: Int): List<ConversationPreview> =
        dao.getAppeals(limit, offset)

    override suspend fun getAppeals(
        limit: Int,
        offset: Int
    ): Response<List<ConversationPreview>> =
        repo.getAppeals(limit, offset)
            .doOnSuccess {
                it.map {
                    dao.setAppeal(it)
                }
            }

    override suspend fun clearAppeals() = dao.clearAppeals()
}