package ru.esstu.student.news.announcement.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import ru.esstu.student.news.announcement.db.announcement.NewsDatabase


class DatabaseDriverFactory: IDatabaseDriverNewsFactory {

    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(NewsDatabase.Schema, "announcement.db")
}

actual fun databaseStudentFactory(): IDatabaseDriverNewsFactory = DatabaseDriverFactory()