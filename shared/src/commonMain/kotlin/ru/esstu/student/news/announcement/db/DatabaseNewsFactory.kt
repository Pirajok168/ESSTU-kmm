package ru.esstu.student.news.announcement.db

import com.squareup.sqldelight.db.SqlDriver

interface IDatabaseDriverFactory {
    val sqlDriver: SqlDriver
}

expect fun createDriver(): IDatabaseDriverFactory