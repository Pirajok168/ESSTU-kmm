package ru.esstu.student.messaging.dialog_chat.datasources.db.chat_history

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver


class DatabaseHistoryCacheFactory():IDatabaseHistoryCacheFactory {
    override val sqlDriver: SqlDriver
        get() = NativeSqliteDriver(MessageWithRelatedTAble.Schema, "announcement.db")
}

actual fun databaseHistoryCacheFactory(): IDatabaseHistoryCacheFactory = DatabaseHistoryCacheFactory()