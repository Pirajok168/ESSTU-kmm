package ru.esstu.student.messaging.group_chat.entities



data class CachedFile(val source: String, val uri: String, val name: String, val ext: String, val size: Long, val type: String) {
    val isImage: Boolean get() = type.contains("image", true)
}