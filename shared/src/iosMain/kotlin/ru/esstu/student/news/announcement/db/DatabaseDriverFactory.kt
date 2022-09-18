package ru.esstu.student.news.announcement.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import ru.esstu.student.NewsDatabase


class DatabaseDriverFactory: IDatabaseDriverNewsFactory {

    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(NewsDatabase.Schema, "test.db")
}

actual fun driverNewsFactory(): IDatabaseDriverNewsFactory = DatabaseDriverFactory()