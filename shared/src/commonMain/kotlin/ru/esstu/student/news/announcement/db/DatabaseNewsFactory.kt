package ru.esstu.student.news.announcement.db

import com.squareup.sqldelight.db.SqlDriver

interface IDatabaseDriverNewsFactory {
    val sqlDriver: SqlDriver
}

expect fun driverNewsFactory(): IDatabaseDriverNewsFactory