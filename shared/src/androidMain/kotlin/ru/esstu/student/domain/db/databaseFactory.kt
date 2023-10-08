package ru.esstu.student.domain.db

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import ru.esstu.ContextApplication
import ru.esstu.student.EsstuDatabase



class DatabaseDriverFactory(
    private val context: Context = ContextApplication.getContextApplication().context
): IDatabaseFactory {
    override val sqlDriver: SqlDriver
        get() = AndroidSqliteDriver(
            EsstuDatabase.Schema,
            context,
            "esstustudent.db",
            callback = object : AndroidSqliteDriver.Callback(EsstuDatabase.Schema) {
                override fun onConfigure(db: SupportSQLiteDatabase) {
                    super.onConfigure(db)
                    db.setForeignKeyConstraintsEnabled(true)
                }
            })

}

actual fun databaseFactory(): IDatabaseFactory = DatabaseDriverFactory()