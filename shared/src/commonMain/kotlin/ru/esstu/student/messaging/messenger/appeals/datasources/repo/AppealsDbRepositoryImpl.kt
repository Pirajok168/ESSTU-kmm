package ru.esstu.student.messaging.messenger.appeals.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.student.messaging.messenger.appeals.datasources.db.AppealsCacheDao
import ru.esstu.student.messaging.messenger.appeals.toMessage
import ru.esstu.student.messaging.messenger.conversations.datasources.toMessage
import ru.esstu.student.messaging.messenger.conversations.entities.ConversationPreview

class AppealsDbRepositoryImpl(
    private val auth: IAuthRepository,
    private val cacheDao: AppealsCacheDao
): IAppealsDbRepository {
    override suspend fun getAppeals(limit: Int, offset: Int): List<ConversationPreview> {
        val dialogs = auth.provideToken {
                token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
            cacheDao.getDialogWithLastMessage(appUserId, pageSize = limit, pageOffset = offset ).map {  it.toMessage() }
        }.data ?: emptyList()
        return dialogs
    }

    override suspend fun clear() =  cacheDao.clear()

    override suspend fun setAppeals(previewDialogs: List<ConversationPreview>) {
        auth.provideToken { token ->
            val appUserId = token.owner.id ?: throw Exception("unsupported User Type")
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