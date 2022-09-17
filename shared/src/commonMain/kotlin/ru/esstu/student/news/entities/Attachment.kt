package ru.esstu.student.news.entities

data class Attachment(
    val id: Int,
    val fileUri: String,

    val loadProgress: Float? = null,
    val localFileUri: String?,

    val name: String?,
    val ext: String? = "",
    val size: Int,
    val type: String?
) {
    val isImage: Boolean get() = type?.contains("image", true) == true
    val closestUri: String get() = localFileUri ?: fileUri
    val nameWithExt: String? get() = name?.let { "$it${ext?.let { ex -> ".$ex" }}" }
    val sizeFormat: String
        get() {
            var resultSize: Float = size.toFloat()
            var sizeName = "Б."

            if (resultSize >= 1000) {
                resultSize /= 1024
                sizeName = "Кб."
            }
            if (resultSize >= 1000) {
                resultSize /= 1024
                sizeName = "Mб."
            }
            if (resultSize >= 1000) {
                resultSize /= 1024
                sizeName = "Гб."
            }
            if (resultSize >= 1000) {
                resultSize /= 1024
                sizeName = "Tб."
            }
            return "%.1f$sizeName"
        }
}