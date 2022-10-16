package ru.esstu.domain.modules.account.datasources.datastore

import okio.FileSystem

class _FileStorage: _FileSystem {
    override val fileSystem: FileSystem
        get() = FileSystem.SYSTEM

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

actual fun storage(): _FileSystem = _FileStorage()


actual fun producePath(): _FileSystem = _FileStorage()