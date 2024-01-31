package ru.esstu.domain.modules.account.datasources.datastore

import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask
import ru.esstu.data.datastore.dataStoreFileName
import ru.esstu.data.fileSystem._FileSystem

class _FileStorage: _FileSystem {
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM

    @OptIn(ExperimentalForeignApi::class)
    override val path: String
        get(){
            val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                directory = NSDocumentDirectory,
                inDomain = NSUserDomainMask,
                appropriateForURL = null,
                create = false,
                error = null,
            )
            return requireNotNull(documentDirectory).path + "/$dataStoreFileName"
        }

}


