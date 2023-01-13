package ru.esstu.student.messaging.group_chat.datasources.db.header

import ru.esstu.student.messaging.group_chat.datasources.db.header.entities.ConversationWithParticipants
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatParticipant

interface HeaderDao {
    suspend fun getParticipants(convId:Int, appUserId:String) :List<GroupChatParticipant>

    suspend fun getConversation(id:Int, appUserId:String): GroupChatConversation?

    suspend fun getConversationWithParticipants(id:Int, appUserId:String): ConversationWithParticipants? {
        val conversationEntity = getConversation(id, appUserId)?:return null

        return ConversationWithParticipants(
            conversation = conversationEntity,
            participants = getParticipants(appUserId = appUserId, convId = id)
        )
    }

    suspend fun deleteConversation(id:Int, appUserId:String)

    suspend fun setConversation(conversation:GroupChatConversation)

    suspend fun setParticipants(participants: List<GroupChatParticipant>)

    suspend fun setConversationWithParticipants(conversation: ConversationWithParticipants){
        deleteConversation(conversation.conversation.id.toInt(), conversation.conversation.appUserId)
        setConversation(conversation = conversation.conversation)
        setParticipants(conversation.participants)
    }
}