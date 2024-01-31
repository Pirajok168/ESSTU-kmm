package ru.esstu.features.messanger.dialogs.domain.interactor

import ru.esstu.data.web.api.model.Response
import ru.esstu.data.web.api.model.doOnSuccess
import ru.esstu.features.messanger.dialogs.data.db.dao.DialogsDao
import ru.esstu.features.messanger.dialogs.data.repository.IDialogsApiRepository
import ru.esstu.features.messanger.dialogs.domain.model.PreviewDialog

class DialogsInteractorImpl(
    private val api: IDialogsApiRepository,
    private val dao: DialogsDao
) : DialogsInteractor {
    override suspend fun getLocalDialogs(limit: Int, offset: Int): List<PreviewDialog> =
        dao.getDialogs(limit, offset)


    override suspend fun getDialogs(limit: Int, offset: Int): Response<List<PreviewDialog>> {


        return api.getDialogs(limit, offset)
            .doOnSuccess {
                it.map { dao.setDialog(it) }

            }

    }

    override suspend fun clearDialogs() =
        dao.clearDialogs()


}