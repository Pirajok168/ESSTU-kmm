package ru.esstu.student.messaging.messenger.dialogs.datasources.repo

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners

import ru.esstu.student.messaging.messenger.dialogs.entities.Dialog
import ru.esstu.student.messaging.messenger.dialogs.datasources.db.CacheDao
import ru.esstu.student.messaging.messenger.dialogs.datasources.toDialogs


class DialogsDbRepositoryImpl  constructor(
    private val auth: IAuthRepository,
    private val cacheDao: CacheDao
) : IDialogsDbRepository {
    private val cachedDialogs: MutableSharedFlow<List<Dialog>> = cacheDao.cachedDialogs
    override val _cachedDialogs: SharedFlow<List<Dialog>> = cachedDialogs


    override suspend fun getDialogs(limit: Int, offset: Int): List<Dialog> {

        val dialogs = auth.provideToken {
                token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")
            cacheDao.getDialogWithLastMessage(appUserId, pageSize = limit, pageOffset = offset ).map {  it.toDialogs() }
        }.data ?: emptyList()
        return dialogs
    }

    override suspend fun clear() = TODO()

    override suspend fun setDialogs(dialogs: List<Dialog>) {

        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")
            dialogs.forEach {
                cacheDao.setLastMessage(it.lastMessage ?: return@provideToken)
                cacheDao.setDialog(appUserId,it)
            }

        }
    }

    override suspend fun deleteDialog(id: String) {
        cacheDao.deleteDialog(id)
    }


}