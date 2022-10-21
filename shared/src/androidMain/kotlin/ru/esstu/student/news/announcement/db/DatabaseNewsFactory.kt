package ru.esstu.student.news.announcement.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication

import ru.esstu.student.news.announcement.db.announcement.NewsDatabase


class DatabaseDriverFactory(
    private val context: Context = ContextApplication.getContextApplication().context
):IDatabaseDriverNewsFactory {
    override val sqlDriver: SqlDriver
        get() = AndroidSqliteDriver(NewsDatabase.Schema, context, "announcement.db")

}

actual fun driverNewsFactory(): IDatabaseDriverNewsFactory = DatabaseDriverFactory()