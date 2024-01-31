package ru.esstu.domain.modules.account.datasources.datastore

import android.content.Context
import android.os.Environment
import okio.FileSystem
import ru.esstu.ContextApplication
import ru.esstu.data.fileSystem._FileSystem

class _FileStorage : _FileSystem {
    private var context: Context = ContextApplication.getContextApplication().context
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM

    override val path: String
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).path
}


