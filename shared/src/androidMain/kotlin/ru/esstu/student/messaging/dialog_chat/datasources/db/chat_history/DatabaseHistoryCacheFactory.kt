package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication
import ru.esstu.student.news.announcement.db.DatabaseDriverFactory
import ru.esstu.student.news.announcement.db.IDatabaseDriverNewsFactory
import ru.esstu.student.news.announcement.db.announcement.NewsDatabase

class DatabaseHistoryCacheFactory(
    private val context: Context = ContextApplication.getContextApplication().context,
    override val sqlDriver: SqlDriver = AndroidSqliteDriver(NewsDatabase.Schema, context, "databaseHistoryCache.db")
): IDatabaseHistoryCacheFactory {

}


actual fun databaseHistoryCacheFactory(): IDatabaseHistoryCacheFactory = DatabaseHistoryCacheFactory()