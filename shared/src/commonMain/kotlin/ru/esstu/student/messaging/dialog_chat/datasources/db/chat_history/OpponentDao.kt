package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity

interface OpponentDao {

    suspend fun getOpponent(id: String): DialogChatAuthorEntity?


    suspend fun insert(opponent: DialogChatAuthorEntity)


    suspend fun clear(id: String)
}