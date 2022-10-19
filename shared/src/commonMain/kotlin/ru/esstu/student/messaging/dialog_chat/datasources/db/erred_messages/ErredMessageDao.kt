package ru.esstu.student.messaging.dialog_chat.datasources.db.erred_messages

import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity

interface ErredMessageDao {

    suspend fun getOpponent(id: String): DialogChatAuthorEntity?


    suspend fun insert(opponent: DialogChatAuthorEntity)


    suspend fun clear(id: String)
}