package ru.esstu.student.news.announcement.datasources.db.timestamp

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import platform.Foundation.NSDownloadsDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import ru.esstu.student.news.TimestampDatabase



class DatabaseDriverTimestamp(
    override val sqlDriver: SqlDriver = NativeSqliteDriver(TimestampDatabase.Schema, "timestamp.db")
) : IDatabaseDriverTimestampFactory{
    fun t(){
        val q = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDownloadsDirectory,
            inDomain = NSUserDomainMask,
            null,
            false,
            null
        )
    }
}


actual fun driverTimestampFactory(): IDatabaseDriverTimestampFactory = DatabaseDriverTimestamp()