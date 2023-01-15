package ru.esstu.student.domain.db

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.group_chat.datasources.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.messanger.conversation.datasources.db.ConversationTable
import ru.esstu.student.messaging.messanger.conversation.datasources.db.MessageConversationTable

import ru.esstu.student.messaging.messanger.dialogs.datasources.db.DialogTableNew
import ru.esstu.student.messaging.messanger.dialogs.datasources.db.MessageTableNew
import ru.esstu.student.messaging.messanger.supports.datasources.db.MessageSuppotTable
import ru.esstu.student.messaging.messanger.supports.datasources.db.SuppotTable
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.news.announcement.datasources.db.announcement.NewsEntityDatabase

import ru.esstu.student.news.announcement.db.announcement.entities.NewsAttachmentEntity
import ru.esstu.student.news.announcement.db.announcement.entities.UserEntity


interface IDatabaseStudent {
    fun getDataBase(): EsstuDatabase
}

class DatabaseStudent(sqlDriver: SqlDriver): IDatabaseStudent {
    private val adapter = object : ColumnAdapter<UserEntity, String> {

        override fun decode(databaseValue: String): UserEntity {
            return Json{ }.decodeFromString(databaseValue)
        }

        override fun encode(value: UserEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val listAdapter = object : ColumnAdapter<List<NewsAttachmentEntity>, String> {
        override fun decode(databaseValue: String): List<NewsAttachmentEntity> {
            return Json{ }.decodeFromString(databaseValue)
        }

        override fun encode(value: List<NewsAttachmentEntity>): String {
            return Json{}.encodeToString(value)
        }

    }

    private val adapter2 = object : ColumnAdapter<DialogChatAuthorEntity, String> {
        override fun decode(databaseValue: String): DialogChatAuthorEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: DialogChatAuthorEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val adapter3 = object : ColumnAdapter<DialogChatAuthorEntity, String> {
        override fun decode(databaseValue: String): DialogChatAuthorEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: DialogChatAuthorEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val adapter4 = object : ColumnAdapter<ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity, String> {
        override fun decode(databaseValue: String): ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val adapter5 = object : ColumnAdapter<ReplyMessageEntity, String> {
        override fun decode(databaseValue: String): ReplyMessageEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: ReplyMessageEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val adapter6 = object : ColumnAdapter<GroupChatAuthorEntity, String> {
        override fun decode(databaseValue: String): GroupChatAuthorEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: GroupChatAuthorEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val database = EsstuDatabase(sqlDriver,
        NewsEntityDatabaseAdapter=NewsEntityDatabase.Adapter(adapter, listAdapter),
        DialogTableNewAdapter = DialogTableNew.Adapter(adapter4),
        MessageTableNewAdapter= MessageTableNew.Adapter(adapter4,adapter5),
        DialogChatMessageTableNewAdapter = DialogChatMessageTableNew.Adapter(adapter2) ,
        DialogChatReplyMessageTableNewAdapter =  DialogChatReplyMessageTableNew.Adapter(adapter3),
        ConversationTableAdapter = ConversationTable.Adapter(adapter4),
        MessageConversationTableAdapter = MessageConversationTable.Adapter(adapter4,adapter5),
        GroupChatMessageAdapter = GroupChatMessage.Adapter(adapter6),
        GroupChatReplyMessageAdapter = GroupChatReplyMessage.Adapter(adapter6),
        GroupChatConversationAdapter = GroupChatConversation.Adapter(adapter6),
        MessageSuppotTableAdapter = MessageSuppotTable.Adapter(adapter4, adapter5),
        SuppotTableAdapter = SuppotTable.Adapter(adapter4)
    )

    override fun getDataBase(): EsstuDatabase = database
}