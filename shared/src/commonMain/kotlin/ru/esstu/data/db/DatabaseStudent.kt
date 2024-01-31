package ru.esstu.data.db

import app.cash.sqldelight.ColumnAdapter
import app.cash.sqldelight.adapter.primitive.IntColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.features.messanger.appeals.data.db.Appeal
import ru.esstu.features.messanger.conversations.data.db.Conversation
import ru.esstu.features.messanger.dialogs.data.db.Dialog
import ru.esstu.features.messanger.supports.data.db.Support
import ru.esstu.features.news.announcement.data.db.entities.NewsAttachmentEntity
import ru.esstu.features.news.announcement.data.db.entities.UserEntity
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.data.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.chathistory.DialogChatReplyMessageTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.erredmessages.ErredCachedFileTableNew
import ru.esstu.student.messaging.dialogchat.datasources.db.usermessage.UserCachedFileTable
import ru.esstu.student.messaging.group_chat.data.db.chat_history.entities.GroupChatAuthorEntity
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatMessage
import ru.esstu.student.messaging.groupchat.datasources.db.chathistory.GroupChatReplyMessage
import ru.esstu.student.messaging.groupchat.datasources.db.erredmessage.GroupChatErredCachedFile
import ru.esstu.student.messaging.groupchat.datasources.db.header.GroupChatConversation
import ru.esstu.student.messaging.groupchat.datasources.db.usermessages.GroupChatUserCachedFileEntity
import ru.esstu.student.messaging.messenger.datasources.db.cache.entities.ReplyMessageEntity
import ru.esstu.student.messaging.messenger.datasources.entities.PreviewLastMessage
import ru.esstu.student.news.announcement.datasources.db.announcement.NewsEntityDatabase


interface IDatabaseStudent {
    fun getDataBase(): EsstuDatabase
}

class DatabaseStudent(sqlDriver: SqlDriver) : IDatabaseStudent {
    private val adapter = object : ColumnAdapter<UserEntity, String> {

        override fun decode(databaseValue: String): UserEntity {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: UserEntity): String {
            return Json { }.encodeToString(value)
        }

    }

    private val listAdapter = object : ColumnAdapter<List<NewsAttachmentEntity>, String> {
        override fun decode(databaseValue: String): List<NewsAttachmentEntity> {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: List<NewsAttachmentEntity>): String {
            return Json {}.encodeToString(value)
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

    private val adapter4 = object :
        ColumnAdapter<ru.esstu.student.messaging.messenger.datasources.db.cache.entities.UserEntity, String> {
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


    private val adapter7 = object : ColumnAdapter<PreviewLastMessage, String> {
        override fun decode(databaseValue: String): PreviewLastMessage {
            return Json { }.decodeFromString(databaseValue)
        }

        override fun encode(value: PreviewLastMessage): String {

            return Json { }.encodeToString(value)
        }

    }

    private val database = EsstuDatabase(
        sqlDriver,
        NewsEntityDatabaseAdapter = NewsEntityDatabase.Adapter(adapter, listAdapter),
        DialogChatMessageTableNewAdapter = DialogChatMessageTableNew.Adapter(adapter2),
        DialogChatReplyMessageTableNewAdapter = DialogChatReplyMessageTableNew.Adapter(
            adapter3,
            IntColumnAdapter
        ),
        GroupChatMessageAdapter = GroupChatMessage.Adapter(adapter6),
        GroupChatReplyMessageAdapter = GroupChatReplyMessage.Adapter(adapter6, IntColumnAdapter),
        GroupChatConversationAdapter = GroupChatConversation.Adapter(adapter6),
        ErredCachedFileTableNewAdapter = ErredCachedFileTableNew.Adapter(IntColumnAdapter),
        GroupChatErredCachedFileAdapter = GroupChatErredCachedFile.Adapter(IntColumnAdapter),
        GroupChatUserCachedFileEntityAdapter = GroupChatUserCachedFileEntity.Adapter(
            IntColumnAdapter
        ),
        UserCachedFileTableAdapter = UserCachedFileTable.Adapter(IntColumnAdapter),
        DialogAdapter = Dialog.Adapter(adapter7, adapter4, IntColumnAdapter),
        ConversationAdapter = Conversation.Adapter(adapter4, IntColumnAdapter),
        PreviewLastMessageAdapter = ru.esstu.features.messanger.conversations.data.db.PreviewLastMessage.Adapter(
            adapter4
        ),
        AppealAdapter = Appeal.Adapter(adapter4, IntColumnAdapter),
        SupportAdapter = Support.Adapter(adapter4, IntColumnAdapter),
    )

    override fun getDataBase(): EsstuDatabase = database
}