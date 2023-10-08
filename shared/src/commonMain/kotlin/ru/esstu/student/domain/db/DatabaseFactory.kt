package ru.esstu.student.domain.db

import app.cash.sqldelight.db.SqlDriver


interface IDatabaseFactory {
    val sqlDriver: SqlDriver
}

expect fun databaseFactory(): IDatabaseFactory