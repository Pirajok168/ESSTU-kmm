package ru.esstu.student.news.announcement.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication
import ru.esstu.student.news.datasources.NewsDatabase

class DatabaseDriverFactory(
    private val context: Context = ContextApplication.getContextApplication().context
):IDatabaseDriverFactory {
    override val sqlDriver: SqlDriver
        get() = AndroidSqliteDriver(NewsDatabase.Schema, context, "test.db")

}

actual fun createDriver(): IDatabaseDriverFactory = DatabaseDriverFactory()