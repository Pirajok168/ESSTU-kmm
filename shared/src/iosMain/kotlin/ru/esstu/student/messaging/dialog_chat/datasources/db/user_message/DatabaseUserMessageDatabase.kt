package ru.esstu.student.messaging.dialog_chat.datasources.db.user_message

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import ru.esstu.student.news.announcement.db.IDatabaseDriverNewsFactory
import ru.esstu.student.news.announcement.db.announcement.NewsDatabase

class DatabaseUserMessageDatabase: IDatabaseUserMessageDatabase {

    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(UserMessageTable.Schema, "databaseUserMessageDatabase.db")
}

actual fun databaseUserMessageDatabase(): IDatabaseUserMessageDatabase = DatabaseUserMessageDatabase()