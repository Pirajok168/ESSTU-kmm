package ru.esstu.student.messaging.dialog_chat.data.db.chat_history

import ru.esstu.student.EsstuDatabase

import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAuthorTableNew

class OpponentDatabase(
    database: EsstuDatabase
) : OpponentDao {
    private val dbQuery = database.opponentTableQueries
    override suspend fun getOpponent(id: String): DialogChatAuthorTableNew? {

        return dbQuery.getOpponent(id).executeAsOneOrNull()
    }

    override suspend fun insert(opponent: DialogChatAuthorTableNew) {
        opponent.apply {
            dbQuery.insert(id, fitstName, lastName, patronymic, summary, photo)
        }
    }

    override suspend fun clear(id: String) {
        TODO("Not yet implemented")
    }
}