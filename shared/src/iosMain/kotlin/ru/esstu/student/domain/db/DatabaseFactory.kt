package ru.esstu.student.domain.db

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import ru.esstu.student.EsstuDatabase

class DatabaseFactory: IDatabaseFactory {

    val dbConfig = DatabaseConfiguration(
        name = "esstustudent.db",
        version = 1,
        create = { connection ->
            wrapConnection(connection) { EsstuDatabase.Schema.create(it) }
        },
        extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
    )
    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(dbConfig)
}

actual fun databaseFactory(): IDatabaseFactory = DatabaseFactory()