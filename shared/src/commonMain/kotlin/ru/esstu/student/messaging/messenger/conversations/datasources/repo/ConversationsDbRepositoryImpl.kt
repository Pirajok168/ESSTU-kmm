package ru.esstu.student.messaging.messenger.conversations.datasources.repo

import ru.esstu.auth.datasources.repo.IAuthRepository
import ru.esstu.auth.entities.TokenOwners
import ru.esstu.student.messaging.messenger.conversations.datasources.db.ConversationsCacheDao
import ru.esstu.student.messaging.messenger.conversations.datasources.toMessage
import ru.esstu.student.messaging.messenger.conversations.entities.Conversation
import ru.esstu.student.messaging.messenger.dialogs.datasources.toMessage

class ConversationsDbRepositoryImpl(
    private val auth: IAuthRepository,
    private val cacheDao: ConversationsCacheDao
) :
    IConversationsDbRepository {
    override suspend fun getConversations(limit: Int, offset: Int): List<Conversation> {
        val dialogs = auth.provideToken {
                token ->
            val appUserId = (token.owner as? TokenOwners.Student)?.id ?: throw Exception("unsupported User Type")
            cacheDao.getDialogWithLastMessage(appUserId, pageSize = limit, pageOffset = offset ).map {  it.toMessage() }
        }.data ?: emptyList()
        return dialogs
    }

    override suspend fun clear() {
        cacheDao.clear()
    }

    override suspend fun setConversations(previewDialogs: List<Conversation>) {
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