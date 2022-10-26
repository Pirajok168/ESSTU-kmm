package ru.esstu.android.student.messaging.dialog_chat.util

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import ru.esstu.student.messaging.dialog_chat.entities.CachedFile
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun Uri.cacheToFile(context: Context): CachedFile? {
    var filename: String? = null
    var fileSize: Long? = null
    val mimeType = context.contentResolver.getType(this) ?: return null

    context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        cursor.moveToFirst()
        filename = cursor.getString(nameIndex)
        fileSize = cursor.getLong(sizeIndex)
    }

    if (filename == null || fileSize == null)
        return null

    val parcelFileDescriptor = context.contentResolver.openFileDescriptor(this, "r", null) ?: return null

    val cachedFile = File(context.cacheDir, filename!!)

    FileOutputStream(cachedFile).use { cachedFileStream ->
        FileInputStream(parcelFileDescriptor.fileDescriptor).use { originalFileStream ->
            originalFileStream.copyTo(cachedFileStream)
        }
    }

    val cachedFileUri = cachedFile.toUri()

    return CachedFile(
        size = fileSize!!,
        ext = cachedFile.extension,
        name = cachedFile.nameWithoutExtension,
        uri = cachedFileUri.toString(),
        type = mimeType,
        sourceFile = cachedFile.path
    )
}

fun withPermissions(context: Context, vararg permissions: String, onRequest: () -> Unit, onGranted: () -> Unit) {
    val hasPermissions = permissions.all { permission ->
        ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    if (hasPermissions) onGranted() else onRequest()
}