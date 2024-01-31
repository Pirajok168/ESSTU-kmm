package ru.esstu.student.messaging.dialog_chat.data.db.chat_history


import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatAuthorTableNew

interface OpponentDao {

    suspend fun getOpponent(id: String): DialogChatAuthorTableNew?


    suspend fun insert(opponent: DialogChatAuthorTableNew)


    suspend fun clear(id: String)
}