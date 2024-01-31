package ru.esstu.data.fileSystem

import ru.esstu.domain.modules.account.datasources.datastore._FileStorage

actual fun storage(): _FileSystem = _FileStorage()
actual fun producePath(): _FileSystem = _FileStorage()