package ru.esstu.student.messaging.entities


data class CachedFile(
    val sourceFile: String,
    val uri: String,
    val name: String,
    val ext: String,
    val size: Long,
    val type: String
) {
    val isImage: Boolean get() = type.contains("image", true)
}