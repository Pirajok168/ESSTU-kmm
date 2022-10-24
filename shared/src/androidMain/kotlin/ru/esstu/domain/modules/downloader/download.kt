package ru.esstu.domain.modules.downloader

import android.os.Environment
import okio.FileSystem
import okio.Path
import java.io.File
import java.io.FileOutputStream


actual suspend fun download(data: ByteArray,  path: Path){
    val path = path
    var file = File.createTempFile("my_file",".pdf", path.toFile())
    var os = FileOutputStream(file)
    os.write(data)
    os.close()
}