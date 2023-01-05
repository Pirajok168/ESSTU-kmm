package ru.esstu.student.messaging.dialog_chat_new.datasources.db.chat_history


import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAuthorTableNew

interface OpponentDao {

    suspend fun getOpponent(id: String): DialogChatAuthorTableNew?


    suspend fun insert(opponent: DialogChatAuthorTableNew)


    suspend fun clear(id: String)
}