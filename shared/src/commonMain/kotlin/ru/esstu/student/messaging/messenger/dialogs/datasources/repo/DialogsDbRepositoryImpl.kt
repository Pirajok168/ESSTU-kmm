package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners

import ru.esstu.student.messaging.messenger.dialogs.entities.PreviewDialog
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toMessage


class DialogsDbRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val cacheDao: CacheDao
) : IDialogsDbRepository {



    override suspend fun getDialogs(limit: Int, offset: Int): List<PreviewDialog> {

        val dialogs = auth.provideToken {
                token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
            cacheDao.getDialogWithLastMessage(appUserId, pageSize = limit, pageOffset = offset ).map {  it.toMessage() }
        }.data ?: emptyList()
        return dialogs
    }

    override suspend fun clear() = cacheDao.deleteAll()

    override suspend fun setDialogs(previewDialogs: List<PreviewDialog>) {

        auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
            previewDialogs.forEach {
                cacheDao.setLastMessage(it.lastMessage ?: return@provideToken)
                cacheDao.setDialog(appUserId,it)
            }

        }
    }

    override suspend fun deleteDialog(id: String) {
        //cacheDao.deleteDialog(id)
    }


}