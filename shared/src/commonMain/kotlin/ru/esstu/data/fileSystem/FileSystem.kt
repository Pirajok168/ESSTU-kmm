package ru.esstu.data.fileSystem

import okio.FileSystem

interface _FileSystem {
    val fileSystem: FileSystem

    val path: String
}