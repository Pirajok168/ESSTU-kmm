package ru.esstu.student.domain.db

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.esstu.student.EsstuDatabase
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.entities.DialogChatAuthorEntity
import ru.esstu.student.messagingdialogchatdatasourcesdb.chathistory.DialogChatMessageTable
import ru.esstu.student.messagingdialogchatdatasourcesdb.chathistory.DialogChatReplyMessageTable
import ru.esstu.student.news.announcement.db.announcement.entities.NewsAttachmentEntity
import ru.esstu.student.news.announcement.db.announcement.entities.UserEntity
import ru.esstu.student.news.announcementdbannouncement.NewsEntityDatabase

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

    private val database = EsstuDatabase(sqlDriver,
        DialogChatMessageTable.Adapter(adapter2),
        DialogChatReplyMessageTable.Adapter(adapter3),
        NewsEntityDatabase.Adapter(adapter, listAdapter),
    )

    override fun getDataBase(): EsstuDatabase = database
}