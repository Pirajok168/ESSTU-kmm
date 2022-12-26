package ru.esstu.student.news.announcement.datasources.db.timestamp

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication
import ru.esstu.student.EsstuDatabase


class DatabaseDriverTimestamp(
    private val context: Context = ContextApplication.getContextApplication().context
): IDatabaseDriverTimestampFactory {
    override val sqlDriver: SqlDriver
        get() = AndroidSqliteDriver(EsstuDatabase.Schema, context, "timestamp.db")
}

actual fun driverTimestampFactory(): IDatabaseDriverTimestampFactory = DatabaseDriverTimestamp()