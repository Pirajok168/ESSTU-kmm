package ru.esstu.student.news.announcement.datasources.db.timestamp

import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.student.news.announcement.db.IDatabaseDriverNewsFactory

interface IDatabaseDriverTimestampFactory {
    val sqlDriver: SqlDriver
}

expect fun driverTimestampFactory(): IDatabaseDriverTimestampFactory