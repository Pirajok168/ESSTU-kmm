package ru.esstu.student.news.announcement.datasources.db.timestamp

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import ru.esstu.student.TimestampDatabase

class DatabaseDriverTimestamp(
    override val sqlDriver: SqlDriver = NativeSqliteDriver(TimestampDatabase.Schema, "timestamp.db")
) : IDatabaseDriverTimestampFactory


actual fun driverTimestampFactory(): IDatabaseDriverTimestampFactory = DatabaseDriverTimestamp()