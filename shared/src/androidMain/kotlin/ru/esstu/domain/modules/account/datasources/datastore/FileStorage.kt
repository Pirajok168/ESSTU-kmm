package ru.esstu.domain.modules.account.datasources.datastore

import android.content.Context
import android.os.Environment
import androidx.datastore.core.Storage
import okio.FileSystem
import okio.ForwardingFileSystem
import ru.esstu.ContextApplication

class _FileStorage(): _FileSystem{
    private  var context: Context = ContextApplication.getContextApplication().context
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM

    override val path: String
        get() = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString()
}

actual fun storage(): _FileSystem = _FileStorage()


actual fun producePath(): _FileSystem = _FileStorage()