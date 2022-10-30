package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection
import ru.esstu.student.news.announcement.db.IDatabaseDriverNewsFactory
import ru.esstu.student.news.announcement.db.announcement.NewsDatabase

class DatabaseUserMessageDatabase: IDatabaseUserMessageDatabase {

    val dbConfig = DatabaseConfiguration(
        name = "databaseUserMessageDatabase.db",
        version = 1,
        create = { connection ->
            wrapConnection(connection) { UserMessageTable.Schema.create(it) }
        },
        extendedConfig = DatabaseConfiguration.Extended(foreignKeyConstraints = true)
    )
    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(dbConfig)
}

actual fun databaseUserMessageDatabase(): IDatabaseUserMessageDatabase = DatabaseUserMessageDatabase()