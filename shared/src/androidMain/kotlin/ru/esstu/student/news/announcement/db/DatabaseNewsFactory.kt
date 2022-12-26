package ru.esstu.student.news.announcement.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.esstu.ContextApplication
import ru.esstu.student.EsstuDatabase


class DatabaseDriverFactory(
    private val context: Context = ContextApplication.getContextApplication().context
):IDatabaseDriverNewsFactory {
    override val sqlDriver: SqlDriver
        get() = AndroidSqliteDriver(EsstuDatabase.Schema, context, "esstustudent.db")

}

actual fun databaseStudentFactory(): IDatabaseDriverNewsFactory = DatabaseDriverFactory()