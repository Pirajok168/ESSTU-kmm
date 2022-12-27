package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.cache.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogWithMessage

import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs
import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog



class DialogsDbRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val cacheDao: CacheDao
) : IDialogsDbRepository {



    override suspend fun getDialogs(limit: Int, offset: Int): List<Dialog> {

        val dialogs = auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")

            cacheDao.getDialogsWithLastMessage(appUserId = appUserId, pageSize = limit, pageOffset = offset).map { it.toDialogs() }
        }.data ?: emptyList()

        return dialogs
    }

    override suspend fun clear() = cacheDao.clearAll()

    override suspend fun setDialogs(dialogs: Map<Int, Dialog>) {

        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")

            cacheDao.setDialogsWithLastMessage(appUserId, dialogs.map { entry ->  entry.value.toDialogWithMessage(sortOrder = entry.key, appUserId = appUserId) })
        }
    }
}