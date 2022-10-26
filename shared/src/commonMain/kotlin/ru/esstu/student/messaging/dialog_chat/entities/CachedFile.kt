package ru.esstu.student.messaging.dialog_chat.entities

import okio.Path


data class CachedFile(val sourceFile: String, val uri: String, val name: String, val ext: String, val size: Long, val type: String) {
    val isImage: Boolean get() = type.contains("image", true)
}