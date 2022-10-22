package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.DatabaseHistoryCacheFactory
import ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history.IDatabaseHistoryCacheFactory
import ru.esstu.student.news.announcement.db.announcement.NewsDatabase

class DatabaseUserMessageDatabase(
    private val context: Context = ContextApplication.getContextApplication().context,
    override val sqlDriver: SqlDriver = AndroidSqliteDriver(UserMessageTable.Schema, context, "databaseUserMessageDatabase.db")
): IDatabaseUserMessageDatabase


actual fun databaseUserMessageDatabase(): IDatabaseUserMessageDatabase = DatabaseUserMessageDatabase()
