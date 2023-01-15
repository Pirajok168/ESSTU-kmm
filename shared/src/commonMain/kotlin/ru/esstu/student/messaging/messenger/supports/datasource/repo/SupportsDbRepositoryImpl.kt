package ru.esstu.student.messaging.messenger.supports.datasource.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.student.messaging.messenger.conversations.datasources.db.ConversationsCacheDao
import ru.esstu.student.messaging.messenger.conversations.datasources.toMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview
import ru.esstu.student.messaging.messenger.supports.datasource.db.SupportsCacheDao
import ru.esstu.student.messaging.messenger.supports.toMessage

class SupportsDbRepositoryImpl(
    private val auth: IAuthRepository,
    private val cacheDao: SupportsCacheDao
): ISupportsDbRepository {
    override suspend fun getSupports(limit: Int, offset: Int): List<ConversationPreview> {
        val dialogs = auth.provideToken {
                token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")
            cacheDao.getDialogWithLastMessage(appUserId, pageSize = limit, pageOffset = offset ).map {  it.toMessage() }
        }.data ?: emptyList()
        return dialogs
    }

    override suspend fun clear() = cacheDao.clear()

    override suspend fun setSupports(previewDialogs: List<ConversationPreview>) {
        auth.provideToken { token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")
            previewDialogs.forEach {
                cacheDao.setLastMessage(it.lastMessage ?: return@provideToken)
                cacheDao.setDialog(appUserId,it)
            }

        }
    }

    override suspend fun deleteDialog(id: String) {
        TODO("Not yet implemented")
    }
}
