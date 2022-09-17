package ru.esstu.student.news.announcement.db

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.news.datasources.NewsDatabase

class DatabaseDriverFactory: IDatabaseDriverFactory {

    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(NewsDatabase.Schema, "test.db")
}

actual fun createDriver(): IDatabaseDriverFactory = DatabaseDriverFactory()