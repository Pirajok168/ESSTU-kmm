package ru.esstu.student.domain.db

import com.squareup.sqldelight.db.SqlDriver


interface IDatabaseFactory {
    val sqlDriver: SqlDriver
}

expect fun databaseFactory(): IDatabaseFactory