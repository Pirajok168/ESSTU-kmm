package ru.esstu.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

import ru.esstu.student.EsstuDatabase

class DatabaseFactory : IDatabaseFactory {

    private val dbConfig = DatabaseConfiguration(
        name = "esstustudent.db",
        version = 1,
        create = { connection ->
            wrapConnection(connection) {
                EsstuDatabase.Schema.create(it)
            }
        },
        extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
    )
    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(dbConfig)
}

actual fun databaseFactory(): IDatabaseFactory = DatabaseFactory()