package ru.esstu.data.db

import app.cash.sqldelight.db.SqlDriver


interface IDatabaseFactory {
    val sqlDriver: SqlDriver
}

expect fun databaseFactory(): IDatabaseFactory