package ru.esstu.student.messaging.group_chat.data.db.header

import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatParticipant

class HeaderDatabase(
    database: EsstuDatabase
) : HeaderDao {

    private val dbQuery = database.headerQueries
    override suspend fun getParticipants(
        convId: Int,
        appUserId: String
    ): List<GroupChatParticipant> {
        return dbQuery.getParticipants(appUserId, convId.toLong()).executeAsList()
    }

    override suspend fun getConversation(id: Int, appUserId: String): GroupChatConversation? {
        return dbQuery.getConversation(appUserId, id.toLong()).executeAsOneOrNull()
    }

    override suspend fun deleteConversation(id: Int, appUserId: String) {
        dbQuery.deleteConversation(appUserId, id.toLong())
    }

    override suspend fun setConversation(conversation: GroupChatConversation) {
        conversation.apply {
            dbQuery.setConversation(id, appUserId, title, author, notifyAboutIt)
        }
    }

    override suspend fun setParticipants(participants: List<GroupChatParticipant>) {
        participants.forEach {
            it.apply {
                dbQuery.setParticipants(
                    idParticipant,
                    appUserId,
                    conversationId,
                    firstName,
                    lastName,
                    patronymic,
                    summary,
                    photo
                )
            }
        }
    }
}