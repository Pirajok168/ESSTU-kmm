package ru.esstu.domain.modules.account.datasources.datastore

import android.content.Context
import androidx.datastore.core.Storage
import okio.FileSystem
import okio.ForwardingFileSystem
import ru.esstu.ContextApplication

class _FileStorage(): _FileSystem{
    private  var context: Context = ContextApplication.getContextApplication().context
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM

    override val path: String
        get() = context.filesDir.resolve(dataStoreFileName).absolutePath
}

actual fun storage(): _FileSystem = _FileStorage()


actual fun producePath(): _FileSystem = _FileStorage()